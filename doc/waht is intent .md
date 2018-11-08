### 在android项目中，intent的作用是什么
在回答这个问题是我们先弄清楚intent是什么---intent是一个消息传递对象，可用于Android组件之间的通信，也可以处理组件的相关请求。
在Android项目中，除ContentProvider组件之外，Activity、Broadcast和Service都是通过intent进行通信的。

* 您可以通过将 Intent 传递到 startActivity() 或 startActivityForResult()（当您想让 Activity 返回结果时）来启动 Activity（或为其安排新任务）。
* 您可以通过将　Intent 传递到 startService() 来启动服务（或对执行中的服务下达新指令）。 或者，您也可以通过将 Intent 传递到 bindService() 来绑定到该服务。
* 您可以通过将 Intent 传递到 sendBroadcast()、sendOrderedBroadcast() 或 sendStickyBroadcast() 等方法来发起广播；
ContentProvider本身就是一种通讯机制，不需要使用intent.<br/>

Intent的组成部分:
* component
* action
* category
* data -- 一个Url对象，setData(),setType(),setDataAndType()
* type
* extras
* Flags

显示的Intent---按名称（component类名）指定要启动的组件。<br/>
隐式的Intent---只申明常规操作(action,category...)，不指定具体的组件。<br/>
