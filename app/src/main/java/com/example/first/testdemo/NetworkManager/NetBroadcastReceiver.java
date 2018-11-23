package com.example.first.testdemo.NetworkManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetEvent event = BaseActivity.event;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            int netWorkState=NetUtil.getNetWorkState(context);
            event.onNetChange(netWorkState);
        }
    }
    // 自定义接口
    public interface NetEvent {
        public void onNetChange(int netMobile);
    }
}
