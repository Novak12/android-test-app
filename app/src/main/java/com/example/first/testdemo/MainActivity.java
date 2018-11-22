package com.example.first.testdemo;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.first.testdemo.service.EchoService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Button takePhoto;
    private ImageView picture;
    private Uri imageUri;
    public static File tempFile;
    private Intent echoServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);

        takePhoto.setOnClickListener(this);

        echoServiceIntent = new Intent(this, EchoService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        if (imageUri != null) {
                            Uri uri = data.getData();
                            imageUri = uri;
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.take_photo:
                    //this.openCamera(this);
                    openGallery();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void openCameraFormer() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File outputImage = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            //保存图片到图库
            this.galleryAddPic(outputImage.getAbsolutePath());

            imageUri = Uri.fromFile(outputImage);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //保存图片到图库(指定文件夹，并非相册)
    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void openCamera(Activity activity) {
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename + ".jpg");
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                imageUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                //检查是否有存储权限，以免崩溃
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    Toast.makeText(this, "请开启存储权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        activity.startActivityForResult(intent, TAKE_PHOTO);
    }

    /*
     * 判断sdcard是否被挂载
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CROP_PHOTO);
    }

    /*
     * 创建并发送notification
     */
    public void notification(PendingIntent pendingIntent, String contentTitle) {
        String id = "my_channel_01";
        String name = "我是渠道名字";
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
                Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
                Log.i("123", mChannel.toString());
                notificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(this, id)
                        .setChannelId(id)
                        .setContentTitle(contentTitle)
                        .setContentText("hahaha")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true).build();
            } else {
                notification = new NotificationCompat.Builder(this, id)
                        .setContentTitle(contentTitle)
                        .setContentText("hahaha")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(true)
                        .setChannelId(id).build();
            }
            notificationManager.notify(111123, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onClickNotification(View v) {
        try {
            Intent intent = new Intent(this, PendingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            this.notification(pendingIntent, "notification");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickRegularActivity(View v) {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, ResultActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        this.notification(resultPendingIntent, "notification to new activity");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickBroadcast(View v) {

        /*创建通知栏的点击事件*/
        Intent notificationIntent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        notificationIntent.setAction("notification_clicked");
        notificationIntent.putExtra(MyBroadcastReceiver.TYPE, 1);
        PendingIntent intent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        /*滑动删除通知栏后，广播监听*/
        Intent deleIntent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        deleIntent.setAction("notification_cancelled");
        deleIntent.putExtra(MyBroadcastReceiver.TYPE, 1);
        PendingIntent deletIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, deleIntent, PendingIntent.FLAG_ONE_SHOT);


        String id = "my_channel_01";
        String name = "我是渠道名字";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
        Notification notification = null;
        Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
        Log.i("123", mChannel.toString());
        notificationManager.createNotificationChannel(mChannel);
        notification = new Notification.Builder(this, id)
                .setChannelId(id)
                .setContentTitle("broadcast notification")
                .setContentText("hahaha")
                .setWhen(System.currentTimeMillis())// 通知栏时间，一般是直接用系统的
                .setContentIntent(intent) //点击事件
                .setDeleteIntent(deletIntent) //滑动事件
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher).build();
        notificationManager.notify(111123, notification);
    }

    public void onStartService(View v) {
        startService(echoServiceIntent);
    }

    public void onStopService(View v) {
        stopService(echoServiceIntent);
    }

    public void onBindService(View v) {
        bindService(echoServiceIntent, this, Context.BIND_AUTO_CREATE);
    }

    public void onUnbindService(View v) {
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.i("MainActivity", "service connected");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.i("MainActivity", "service disconnected");
    }

    public void onShowDialog(View v) {
        Intent intent = new Intent(this, DialogActivity.class);
        startActivity(intent);
    }

    public void onShowStorageActivity(View v) {
        Intent intent = new Intent(this, StorageActivty.class);
        startActivity(intent);
    }
}
