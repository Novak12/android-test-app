### file api
读取文件免不了要与file api打交道，对于大多数系统而言，file的处理机制都是一个基础部分，那就来看看android的file处理。

### 存储区域
所有的android系统都有两个存储区域：内部存储和外部存储。<br/>
* 内部存储：一直可用；保存的文件只能供本app使用；当app卸载时，内部存储的文件可会一起被删除。
* 外部存储：并不是一直可用；外部可读，并不是你的app所独有的；当app卸载时，只有保存在getExternalFilesDir()目录下的文件会被删除。

### 内部存储的文件的读写
1. 保存路径
* getFilesDir() ---返回app的内部目录
* getCacheDir() ---返回app的一个临时的缓存目录，当文件不使用时记得将其删除，且文件大小不要超过1MB。

可以使用File()来创建文件：
```javascript
File file = new File(context.getFilesDir(), filename);
```
保存文件的一般用法：
```javascript
String filename = "myfile";
String fileContents = "Hello world!";
FileOutputStream outputStream;

try {
    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
    outputStream.write(fileContents.getBytes());
    outputStream.close();
} catch (Exception e) {
    e.printStackTrace();
}
```
保存文件到缓存中：
```javascript
private File getTempFile(Context context, String url) {
    File file;
    try {
        String fileName = Uri.parse(url).getLastPathSegment();
        file = File.createTempFile(fileName, null, context.getCacheDir());
    } catch (IOException e) {
        // Error while creating file
    }
    return file;
}
```

### 保存文件到外部存储
#### 1.文件类型
* PubLic 文件的访问不受限制
* Private 文件属于app，且当app卸载时，将文件删除；外界可以访问该文件，因为是保存在外部存储

#### 2.访问权限<br/>
下面还要写文件到外部存储，必须要拥有外部存储的读写权限:
```javascript
<manifest ...>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    ...
</manifest>
```

#### 3.检查外部存储是否可用<br/>
可以通过获取外部存储的状态来判断其是否可用：
```javascript
/* Checks if external storage is available for read and write */
public boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
        return true;
    }
    return false;
}

/* Checks if external storage is available to at least read */
public boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) ||
        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        return true;
    }
    return false;
}
```
#### 4.保存文件到外部公共目录
```javascript
public File getPublicAlbumStorageDir(String albumName) {
    // Get the directory for the user's public pictures directory.
    File file = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), albumName);
    if (!file.mkdirs()) {
        Log.e(LOG_TAG, "Directory not created");
    }
    return file;
}
```
通过getExternalStoragePublicDirectory方法来获取外部存储的公共目录。

#### 5.保存文件到外部私有目录
```javascript
public File getPrivateAlbumStorageDir(Context context, String albumName) {
    // Get the directory for the app's private pictures directory.
    File file = new File(context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES), albumName);
    if (!file.mkdirs()) {
        Log.e(LOG_TAG, "Directory not created");
    }
    return file;
}
```


