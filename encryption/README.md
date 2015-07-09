#Demo encryption

Encryption demonstrates how to use javax.crypto in activities.

Secret data is stored encrypted in default preferences using a hard coded key when leaving the activity.
Therefore, there are two states:
1. no encrypted data is stored and you have to re-enter the activity
1. encrypted data is stored and you see both the encrypted and decrypted data.

The demo can be used to see how the auto backup mechanism in Android M works with private data:

Connect your M device, install the app
```
gw encryption:installDebug
```
and store the data. Then run the backup manager as decribed in the [preview documents](http://developer.android.com/preview/backup/index.html):
```
adb shell bmgr run
adb shell bmgr fullbackup com.novoda.demo.encryption
```
Delete the preferences in the app and restore the data with
```
adb shell bmgr restore com.novoda.demo.encryption
```
Now you can continue with different transport services and different keys.

