### android project 
APK:一个apk文件包含android应用的所有文件。

android app:
* android操作系统是一种多用户linux操作系用，么一个应用都是一个不同的用户。
* 默认情况下，系统会为每个应用分配一个唯一的 Linux 用户 ID。系统为应用中的所有文件设置权限，使得只有分配给该应用的用户 ID 才能访问这些文件；
* 每个进程都具有自己的虚拟机 (VM)，因此应用代码是在与其他应用隔离的环境中运行；
* 默认情况下，每个应用都在其自己的 Linux 进程内运行。

### 应用中的组件
* Activity 表示用户界面的单一屏幕
* Service 执行与后台的组件
* ContentProvider 管理一组共享的应用数据（如联系人，记事本等）。
* Broadcast 一种用于响应系统范围广播通知的组件.广播接收器更常见的用途只是作为通向其他组件的“通道”，设计用于执行极少量的工作.广播接收器作为 BroadcastReceiver 的子类实现，并且每条广播都作为 Intent 对象进行传递。

组件的启动：
* 您可以通过将 Intent 传递到 startActivity() 或 startActivityForResult()（当您想让 Activity 返回结果时）来启动 Activity（或为其安排新任务）。
* 您可以通过将　Intent 传递到 startService() 来启动服务（或对执行中的服务下达新指令）。 或者，您也可以通过将 Intent 传递到 bindService() 来绑定到该服务。
* 您可以通过将 Intent 传递到 sendBroadcast()、sendOrderedBroadcast() 或 sendStickyBroadcast() 等方法来发起广播；
* 您可以通过在 ContentResolver 上调用 query() 来对内容提供程序执行查询。

### 清单文件--AndroidManifest.xml
