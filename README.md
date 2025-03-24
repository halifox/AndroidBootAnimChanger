# Android 开机动画替换示例程序

该示例程序展示了如何在 Android 应用中替换系统开机动画。

> 核心是需要通过 adb remount 挂载设备的 /system 分区      
> 因为在应用内`mount -o remount,rw /system`是没有效果的

## 致谢
- [tananaev/adblib](https://github.com/tananaev/adblib)
- [cgutman/AdbLib](https://github.com/cgutman/AdbLib)
- [cstyan/adbDocumentation](https://github.com/cstyan/adbDocumentation)
- [adb/protocol.txt](https://android.googlesource.com/platform/system/core/+/dd7bc3319deb2b77c5d07a51b7d6cd7e11b5beb0/adb/protocol.txt)