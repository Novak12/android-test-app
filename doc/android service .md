### service
在android系统中，service是一个可以在后台长时间运行操作但不提供用户界面的组件。service一旦启动就睡在后台无限期的运行。

### service的生命周期
onCreate()
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
