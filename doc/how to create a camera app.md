### 1.添加camera feature
```javascript
<manifest ... >
    <uses-feature android:name="android.hardware.camera"
                  android:required="true" />
    ...
</manifest>
```
user-feature的作用就像是一个过滤器，google play商店会根据该标签过滤设备，如果将require设置为true，则在google play商店中该app就不再对没有照相机的设备显示。<br/>

### 2.在camera app中如何拍照
Android将操作委托给其他应用程序的方法是调用一个Intent，该Intent描述了您想要做什么。<br/>
这个过程包含三个部分：<br/>
* Intent
* 启动外部Activity,
* 当焦点返回时，用来处理图像数据的代码
该函数就是迎来调用intent来获取图片
```javascript
static final int REQUEST_IMAGE_CAPTURE = 1;

private void dispatchTakePictureIntent() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
}
```
resolveActivity函数的作用是根据intend去收集需要启动的actity的信息。<br/>

### 获取缩略图（thumbnail）
如果拍照这一简单的壮举并不是你的应用程序雄心壮志的顶峰，那么你可能想要从相机应用程序中取回图像并对它做些什么。<br/>
Android Camera解码照片之后，会将intent（包含照片信息）中，在onActivityResult中，将图片信息中将图片信息放入到extras中：
```javascript
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(imageBitmap);
    }
}
```




