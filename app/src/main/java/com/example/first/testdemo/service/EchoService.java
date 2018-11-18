package com.example.first.testdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class EchoService extends Service {
    private final IBinder mBinder = new LocalBinder();
    public EchoService() {
    }
    public class LocalBinder extends Binder {
        EchoService getService() {
            // Return this instance of LocalService so clients can call public methods
            return EchoService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i("EchoService","onBind");
        return mBinder;
    }

    @Override
    public void onCreate(){
        Log.i("EchoService","onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EchoService","onDestory");
    }
}
