# mine-tv
Mine Tv

Android TV 上的影视播放软件

播放资源我没有，在网上随便找了一个，地址见：RequestHelper.BASE_URL 。
如果这个地址不行了，可以换一个，支持 MacCms10 接口的，就可以。
只可以播放 m3u8 格式的源，其他的暂时没开发。

[APP 截图]('.docs/screenshot-v2.md' "APP 截图")

代码是直接使用 Android TV 官方示例 leanback 库的示例进行开发的，只是能用，并不算上好用，后边会逐步替换掉。

当贝F3遥控器对应的按键：

```
adb -s xxxx shell input keyevent 4
.
返回： KeyEvent.KEYCODE_BACK; //4
菜单： KeyEvent.KEYCODE_MENU; //82
语音： KeyEvent.KEYCODE_F5; //135

↑： KeyEvent.KEYCODE_DPAD_UP; //19
↓： KeyEvent.KEYCODE_DPAD_DOWN; // 20
←： KeyEvent.KEYCODE_DPAD_LEFT; // 21
→： KeyEvent.KEYCODE_DPAD_RIGHT; // 22

确定： KeyEvent.KEYCODE_ENTER;  // 66

音量+： KeyEvent.KEYCODE_VOLUME_UP; // 24
音量-： KeyEvent.KEYCODE_VOLUME_DOWN; // 25
```
