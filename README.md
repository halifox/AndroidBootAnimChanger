# Android 开机动画替换示例程序

该示例程序展示了如何在 Android 应用中替换系统开机动画。

> 核心是需要通过 adb remount 挂载设备的 /system 分区      
> 因为在应用内`mount -o remount,rw /system`是没有效果的


## 核心代码
```kotlin
//https://github.com/tananaev/adblib
compile 'com.tananaev:adblib:1.3'
```
```kotlin
  val socket = Socket("127.0.0.1", 5555)
  val crypto = AdbCrypto.generateAdbKeyPair { data -> Base64.encodeToString(data, Base64.NO_WRAP) }
  val connection = AdbConnection.create(socket, crypto)
  connection.connect()
  connection.open("remount:")
  connection.open("shell:cp $bootanimation /system/media/bootanimation.zip")
  connection.open("shell:chmod 644 /system/media/bootanimation.zip")
  connection.open("shell:chown root:root /system/media/bootanimation.zip")
```

## 致谢
- [tananaev/adblib](https://github.com/tananaev/adblib)
- [cgutman/AdbLib](https://github.com/cgutman/AdbLib)
- [cstyan/adbDocumentation](https://github.com/cstyan/adbDocumentation)
- [adb/protocol.txt](https://android.googlesource.com/platform/system/core/+/dd7bc3319deb2b77c5d07a51b7d6cd7e11b5beb0/adb/protocol.txt)
