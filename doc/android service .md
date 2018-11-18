### service
在android系统中，service是一个可以在后台长时间运行操作但不提供用户界面的组件。service一旦启动就睡在后台无限期的运行。

### service的生命周期
onCreate() <br/>
onDestory()
### service 的使用过程
* startService()启动服务
* stopService()停止服务
* bindService()组件绑定service  
```javascript
//在activity中启动一个service
Intent intent = new Intent(this, HelloService.class);
startService(intent);
```

### service中的方法
* startCommand() 当一个组件activity调用了startActivity()方法之后，系统会调用该方法。
* onBind() 当组件调用了bindService()之后，系统会自动的调用该方法。

### bindService()
当组件要绑定一个service时，会触发service的Onbind()方法。如果组件想通过onServiceConnected()事件来监听绑定成功，就需要在service的onBind()的方法中返回一个不为null的Ibinder对象，否则onServiceConnected()不会被调用。
```javascript
public class LocalService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public int getRandomNumber() {
      return mGenerator.nextInt(100);
    }
}
```

