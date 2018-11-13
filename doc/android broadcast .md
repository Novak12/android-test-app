### android broadcast
broadcast是android的四大组件之一，它是一种响应系统范围内广播通知的组件，它类似于发布-订阅这种设计模式。当相关的事件发生时，广播就会被发送。

### receiving broadcasts
app有两种方法来接受广播：通过manifest声明receiver；在代码中动态的注册receiver.
* 在manifest中声明receiver
```javascript
<receiver android:name=".MyBroadcastReceiver"  android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
        <action android:name="android.intent.action.INPUT_METHOD_CHANGED" />
    </intent-filter>
</receiver>
```

