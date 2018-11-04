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

### 保存full-size照片
在manifest文件中添加手机内存写文件权限：
```javascript
<manifest ...>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    ...
</manifest>
```
共享照片的保存目录由getExternalStoragePublicDirectory()来提供，参数为DIRECTORY_PICTURES。<br/>
如果想将图片私有的保存在手机中，可以使用getExternalFilesDir()。在Android4.3及更低的版本中，使用该方法需要WRITE_EXTERNAL_STORAGE权限，当时在Android4.4及以上的版本中，不需要该权限，应为该目录其它app中无法访问的。
使用例子如下：
```javascript
String mCurrentPhotoPath;

private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = image.getAbsolutePath();
    return image;
}
```
有了创建保存图片的文件的方法，可以使用创建和调用intent的例子如下：
```javascript
static final int REQUEST_TAKE_PHOTO = 1;

private void dispatchTakePictureIntent() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Ensure that there's a camera activity to handle the intent
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            ...
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                                                  "com.example.android.fileprovider",
                                                  photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }
}
```
现在还需要配置FileProvider:
```javascript
<application>
   ...
   <provider
        android:name="android.support.v4.content.FileProvider"
        android:authorities="com.example.android.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths"></meta-data>
    </provider>
    ...
</application>
```
一定要确保权限配置一定要getUriForFile(Context, String, File)方法的第二个参数。在meta-data部分，您可以看到，提供者希望在专用资源文件中配置合适的路径，res/xml/file_paths.xml.实例如下：
```javascript
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="my_images" path="Android/data/com.example.package.name/files/Pictures" />
</paths>
```
### 添加照片到图库
当通过Intent生成图片后，并且已经知道了图片的具体位置，对于其他人来说，让您的照片易于访问的最简单方法可能是让它可以从系统的媒体提供者访问。<br/>
下面的示例方法演示了如何调用系统的媒体扫描程序将照片添加到媒体提供者的数据库中，使其在Android Gallery应用程序和其他应用程序中可用。
```javascript
private void galleryAddPic() {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(mCurrentPhotoPath);
    Uri contentUri = Uri.fromFile(f);
    mediaScanIntent.setData(contentUri);
    this.sendBroadcast(mediaScanIntent);
}
```
### 解码缩放图片
在内存有限的情况下，管理多个full-size的图片可能很棘手。如果您发现您的应用程序在显示一些图像之后耗尽了内存，那么您可以通过将JPEG扩展到一个已经按比例缩放以匹配目标视图大小的内存数组来大大减少动态堆的使用量。
```javascript
private void setPic() {
    // Get the dimensions of the View
    int targetW = mImageView.getWidth();
    int targetH = mImageView.getHeight();

    // Get the dimensions of the bitmap
    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    bmOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    int photoW = bmOptions.outWidth;
    int photoH = bmOptions.outHeight;

    // Determine how much to scale down the image
    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

    // Decode the image file into a Bitmap sized to fill the View
    bmOptions.inJustDecodeBounds = false;
    bmOptions.inSampleSize = scaleFactor;
    bmOptions.inPurgeable = true;

    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    mImageView.setImageBitmap(bitmap);
}
```

