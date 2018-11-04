### open the camera object
首先，获取camera instance,正如Android自己的相机应用程序所做的那样，推荐的访问相机的方式是在onCreate()启动的单独线程上打开相机。<br/>
他的方法是一个好主意，因为它可能需要一段时间，并且可能使UI线程陷入停滞。<br/>
在更基本的实现中，打开摄像机可以延迟到onResume()方法，以方便代码重用和保持简单的控制流。<br/>

如果相机已经被其他应用程序使用，那么调用camera .open()会抛出异常，因此我们将其封装在try块中。
```javascript
private boolean safeCameraOpen(int id) {
    boolean qOpened = false;

    try {
        releaseCameraAndPreview();
        mCamera = Camera.open(id);
        qOpened = (mCamera != null);
    } catch (Exception e) {
        Log.e(getString(R.string.app_name), "failed to open Camera");
        e.printStackTrace();
    }

    return qOpened;
}

private void releaseCameraAndPreview() {
    mPreview.setCamera(null);
    if (mCamera != null) {
        mCamera.release();
        mCamera = null;
    }
}
```
### create the camera preview
拍一张照片通常要求用户在点击快门前先预览一下拍摄对象。要做到这一点，您可以使用SurfaceView来绘制摄像机传感器采集的内容的预览。
### Preview Class
该preview class需要实现android.view.SurfaceHolder.Callback接口，它用于将图像数据从摄像机硬件传递到应用程序。<br/>
```javascript
class Preview extends ViewGroup implements SurfaceHolder.Callback {

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;

    Preview(Context context) {
        super(context);

        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
...
}
```
preview类必须传递给Camera对象，才能启动实时image preview，如下一节所示。
### 设置并启动preview
必须以特定的顺序创建camera实例及其相关preview，其中camera对象是第一个。在下面的代码片段中，初始化camera的过程被封装，以便每当用户做了一些事情来更改camera时，setCamera()方法就会调用camera.startpreview()方法。preview还必须在preview类surfaceChanged()回调方法中重新启动。
```javascript
public void setCamera(Camera camera) {
    if (mCamera == camera) { return; }

    stopPreviewAndFreeCamera();

    mCamera = camera;

    if (mCamera != null) {
        List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
        mSupportedPreviewSizes = localSizes;
        requestLayout();

        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Important: Call startPreview() to start updating the preview
        // surface. Preview must be started before you can take a picture.
        mCamera.startPreview();
    }
}
```
### 修改camera设置
示例：
```javascript
@Override
public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    // Now that the size is known, set up the camera parameters and begin
    // the preview.
    Camera.Parameters parameters = mCamera.getParameters();
    parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
    requestLayout();
    mCamera.setParameters(parameters);

    // Important: Call startPreview() to start updating the preview surface.
    // Preview must be started before you can take a picture.
    mCamera.startPreview();
}
```
### 设置preview方向
大多数相机应用程序将显示器锁定在横向模式，因为这是相机传感器的自然方向。这个设置并不妨碍您拍摄肖像模式的照片，因为设备的方向是在EXIF报头中记录的。setCameraDisplayOrientation()方法允许您更改preview的显示方式，而不影响图像的记录方式。但是，在API 14之前的Android中，在更改方向并重新启动之前必须停止preview。<br/>
### 拍照
使用Camera.takePicture()方法在preview开始后拍照。你可以创建使用Camera.PictureCallback和使用Camera.ShutterCallback对象，并将它们传递给Camera.takePicture()。<br/>
如果你想连续抓取图像，你可以创建一个实现onPreviewFrame()的Camera.PreviewCallback。对于介于两者之间的内容，您只能捕获选定的preview框架，或者设置一个延迟动作来调用takePicture()。
### 重启preview
在拍完照片后，你必须重启preview，然后用户才能再拍一张照片。在本例中，重启是通过重载快门按钮来完成的。
```javascript
@Override
public void onClick(View v) {
    switch(mPreviewState) {
    case K_STATE_FROZEN:
        mCamera.startPreview();
        mPreviewState = K_STATE_PREVIEW;
        break;

    default:
        mCamera.takePicture( null, rawCallback, null);
        mPreviewState = K_STATE_BUSY;
    } // switch
    shutterBtnConfig();
}
```
### 停止preview并释放camera
一旦你的应用程序用完摄像头，就该清理了。特别是，您必须释放Camera对象，否则可能会导致其他应用程序崩溃，包括您自己的应用程序的新实例。
什么时候停止预览并释放相机?销毁preview surface是一个很好的提示，是时候停止preview并释放摄像机了，正如preview类中的这些方法所示。
```javascript
@Override
public void surfaceDestroyed(SurfaceHolder holder) {
    // Surface will be destroyed when we return, so stop the preview.
    if (mCamera != null) {
        // Call stopPreview() to stop updating the preview surface.
        mCamera.stopPreview();
    }
}

/**
 * When this function returns, mCamera will be null.
 */
private void stopPreviewAndFreeCamera() {

    if (mCamera != null) {
        // Call stopPreview() to stop updating the preview surface.
        mCamera.stopPreview();

        // Important: Call release() to release the camera for use by other
        // applications. Applications should release the camera immediately
        // during onPause() and re-open() it during onResume()).
        mCamera.release();

        mCamera = null;
    }
}
```
