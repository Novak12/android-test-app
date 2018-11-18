### android activity
activity是android的四大组件之一，它就是一个个的页面，也是android的最基本最重要的组件

### activity的生命周期
onCreate() <br/>
onStart() <br/>
onResume() <br/>
onPause() <br/>
onStop() <br/>
onDestroy() <br/>
以上方法就是activity生命周期中的每一个事件。
### 如何创建一个新的activity
```javascript
Intent intent = new Intent(this, SignInActivity.class);
startActivity(intent);
```
Intent的第一个参数是指当前的activity，第二个参数是指要打开的新的activity。
