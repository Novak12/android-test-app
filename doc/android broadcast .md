### android broadcast
broadcast是android的四大组件之一，它是一种响应系统范围内广播通知的组件，它类似于发布-订阅这种设计模式。当相关的事件发生时，广播就会被发送。

### receiving broadcasts
app有两种方法来接受广播：通过manifest声明receiver；在上下文中动态的注册receiver.
* 在manifest中声明receiver
```javascript
<receiver android:name=".MyBroadcastReceiver"  android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
        <action android:name="android.intent.action.INPUT_METHOD_CHANGED" />
    </intent-filter>
</receiver>
```
MyBroadcastReceiver的内容如下：
```javascript
public class MyBroadcastReceiver extends BroadcastReceiver {
        private static final String TAG = "MyBroadcastReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            StringBuilder sb = new StringBuilder();
            sb.append("Action: " + intent.getAction() + "\n");
            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
            String log = sb.toString();
            Log.d(TAG, log);
            Toast.makeText(context, log, Toast.LENGTH_LONG).show();
        }
    }
```
上面的代码表明，安装应用程序时，系统包管理器注册接收器。然后接收器成为应用程序的独立入口点，这意味着如果应用程序当前没有运行，系统可以启动应用程序并发送广播。<br/>
系统创建一个新的BroadcastReceiver组件对象来处理它接收的每个广播。此对象仅在对onReceive(Context,Intent)的调用期间有效。一旦代码从此方法返回，系统就认为组件不再活动。

* 在上下文中注册receicers
```javascript
BroadcastReceiver br = new MyBroadcastReceiver();

IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
    this.registerReceiver(br, filter);
```
上下文注册的接收者在其注册上下文有效时接收广播。例如，如果您在活动上下文中注册，那么只要活动没有被销毁，您就会收到广播。如果您注册了应用程序上下文，那么只要应用程序在运行，您就会收到广播。

