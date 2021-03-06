### what is android notification?
Notification又称为通知，是一种全局效果的通知，它展示在手机屏幕的顶端，当用户下滑时展示通知的具体内容。

### how to create a basic notification?
```javascript
NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
```
从上面的代码中可以看到，通过一个NotificationCompat.Builder对象来创建事件通知。从android8.0及其以后的版本中，其构造函数中必须传入一个CHANNEL_ID，
这在之前的版本中是不需要的。<br/>
在默认的情况下，通知的内容一般为一行简洁的文字内容，但是仍然有办法可以让你写更多的文字：
```javascript
NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle("My notification")
        .setContentText("Much longer text that cannot fit one line...")
        .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
```
使用setStyle()方法来使用大文本的类型，可以使你放入更多的文字。<br/>

### create a notification channel
```javascript
private void createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
```
在android8.0及更高的版本中，要发布任何通知都是必须要创建一个NotificationChannel.



