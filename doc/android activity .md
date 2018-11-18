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
Intent的第一个参数是指当前的activity，第二个参数是指要打开的新的activity。<br/>
* 如果想在打开新的activity时传递一些数据，可以按照如下操作：
```javascript
Intent intent = new Intent(Intent.ACTION_SEND);
intent.putExtra(Intent.EXTRA_EMAIL, recipientArray);
startActivity(intent);
```
* 如果想在新的activity关闭时，回传一些数据，也可以按照如下操作：
```javascript
public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
             // When the user center presses, let them pick a contact.
             startActivityForResult(
                 new Intent(Intent.ACTION_PICK,
                 new Uri("content://contacts")),
                 PICK_CONTACT_REQUEST);
            return true;
         }
         return false;
     }
     
protected void C(int requestCode, int resultCode,
             Intent data) {
         if (requestCode == PICK_CONTACT_REQUEST) {
             if (resultCode == RESULT_OK) {
                 // A contact was picked.  Here we will just display it
                 // to the user.
                 startActivity(new Intent(Intent.ACTION_VIEW, data));
             }
         }
     }
```
在上面的代码中，使用了startActivityForResult(Intent, int)方法来回去child activity回传的数据，回传的数据通过onActivityResult(int, int, Intent)方法来处理，在使用时要注意result code的使用。child activity回传数据时会使用setResult(int resule_code, Intent intent)方法，第二个参数是可选的，intent参数中就能够携带一些数据了。
