package com.example.first.testdemo.NetworkManager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends Activity implements NetBroadcastReceiver.NetEvent {
    public static NetBroadcastReceiver.NetEvent event;
    private int netMobile;
    @Override
    protected void onCreate(Bundle arg0){
        super.onCreate(arg0);
        event = this;
        inspectNet();
    }
    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }

    //判断初始化是有没有网络
    public boolean inspectNet(){
        this.netMobile = NetUtil.getNetWorkState(BaseActivity.this);
        return isNetConnect();
    }

    //判断有无网络
    public boolean isNetConnect(){
        if(netMobile==-1){
            return false;
        }else if(netMobile==0){
            return true;
        }else if(netMobile==1){
            return true;
        }
        return false;
    }

}
