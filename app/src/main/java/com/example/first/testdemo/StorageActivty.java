package com.example.first.testdemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_activy);
    }

    public void onSaveInfo(View v) {
        String filename = "hello_file.txt";
        String str = "hello world!";
        try {
            //获取外部存储卡的可用状态
            String storageState = Environment.getExternalStorageState();
            //判断是否存在可用的的SD Card
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                //路径： /storage/emulated/0/Android/data/com.example.first.demo/cache/yoryky.txt
                File file = new File(getExternalCacheDir().getAbsolutePath(), filename);
                if (file.exists()) file.delete();

                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(str.getBytes());
                outputStream.close();
                Toast.makeText(this, "文件保存成功", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onReadInfo(View v) {
        String fileName = "hello_file.txt";
        StringBuilder sb = new StringBuilder("");
        try {
            String filePath = getExternalCacheDir().getAbsolutePath();
            File file = new File(filePath, fileName);
            if (file.exists()) {
                FileInputStream fs = new FileInputStream(file);
                int length = fs.available();
                byte[] buffer = new byte[length];
                int len = fs.read(buffer);

                while (len > 0) {
                    sb.append(new String(buffer, 0, len));
                    //继续将数据放到buffer中
                    len = fs.read(buffer);
                }
                fs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
