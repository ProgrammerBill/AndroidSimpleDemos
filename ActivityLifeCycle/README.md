Activity的生命周期如下图所示:

![](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549379239&di=692c7cea8c98c125d693e22225901fb4&imgtype=jpg&er=1&src=http%3A%2F%2Fs11.sinaimg.cn%2Fmiddle%2F70677d11g9f81ed63ba6a%26amp%3B690)

正常启动的时候，Activity的启动流程如下:

1. onCreate
2. onStart
3. onResume

其中onCreate负责处理程序初始化工作，如界面资源的初始化，数据初始化等。onStart表明Activity正在启动，此时Activity可见但不在前台，而onResume表示Activity在前台且可与用户进行交互（当在onResume加上延时后，发现第一次启动时onResume完成后，控件才显示出来）。


观察Activity的测试程序:
```
01-29 21:59:58.681 22123 22123 D ActivityLifeCycle: onCreate
01-29 21:59:58.688 22123 22123 D ActivityLifeCycle: onStart
01-29 21:59:58.692 22123 22123 D ActivityLifeCycle: onResume
```

当按下返回键进入Launcher时或者锁屏时,进行如下流程:

1. onPause
2. onStop

```
01-29 22:15:47.796 23071 23071 D ActivityLifeCycle: onPause
01-29 22:15:48.375 23071 23071 D ActivityLifeCycle: onStop
```

当再次点开该Activity时,不会再调用onCreate:

```
01-30 09:52:10.554 31784 31784 D ActivityLifeCycle: onStart
01-30 09:52:10.556 31784 31784 D ActivityLifeCycle: onResume
```

当在该Activity跳转到别的Activity时:

```
01-30 09:56:28.517 32182 32182 D ActivityLifeCycle: onPause
01-30 09:56:28.898 32182 32182 D ActivityLifeCycle: onStop
```

当返回到该Activity时:

```
01-30 09:56:41.939 32182 32182 D ActivityLifeCycle: onStart
01-30 09:56:41.940 32182 32182 D ActivityLifeCycle: onResume
```


当屏幕进行旋转时，观察可得Activity会先销毁当前的Activity,在重新创建一个新的，并在onResume前，调用onRestoreInstance

```
01-30 10:00:01.089 32483 32483 D ActivityLifeCycle: onPause
01-30 10:00:01.090 32483 32483 D ActivityLifeCycle: onStop
01-30 10:00:01.091 32483 32483 D ActivityLifeCycle: onDestroy
01-30 10:00:01.236 32483 32483 D ActivityLifeCycle: onCreate
01-30 10:00:01.241 32483 32483 D ActivityLifeCycle: onStart
01-30 10:00:01.242 32483 32483 D ActivityLifeCycle: onRestoreInstance
01-30 10:00:01.243 32483 32483 D ActivityLifeCycle: onResume
```

当AndroidManifest.xml中增添了`android:configChanges="orientation|screenSize"`后，则只会回调onConfigurationChanged:


```
01-30 10:01:07.773   432   432 D ActivityLifeCycle: onConfigurationChanged
```
