四种启动模式：Standerd、SingleTop、SingleTask、SingleInstance。

- Standard(默认标准启动模式):每次启动都重新创建一个新的实例，不管它是否存在。且谁启动了这个Acitivity,那么这个Acitivity就运行在启动它的那个Acitivity的任务栈中。

- SingleTop(栈顶复用模式):如果新的Activity已经位于任务栈的栈顶，那么不会被重新创建，而是回调onNewIntent()方法，通过此方法的参数可以取出当前请求的信息。

- SingleTask(栈内复用模式):这是一种单例模式，在这种模式下，只要Acitivity在一个栈中存在，那么多次启动此Acitivity都不会重建实例，而是回调onNewIntent方法。同时由于SingleTask模式有ClearTop功能，因此会导致所要求的Acitivity上方的Acitivity全部销毁。

- SingleInstance(单实例模式):和栈内复用类似，此种模式的Acitivity只能单独位于一个任务栈中。全局唯一性。单例实例，不是创建，而是重用。独占性，一个Acitivity单独运行在一个工作栈中。

问题:

如果假设A是Standard，B是SingleTop，C是SingleTask，D是SingleInstance的启动模式，那么以A->B->C->D->A->B->C->D这种情况开启Activity，分析一下最后的工作栈是怎样的情况？


```
HWHRY-HF:/ $ logcat | grep -i "activity:"
//Step 1: Standard模式A创建:A->onCreate->onStart->onResume
01-29 00:18:52.600  5935  5935 D StandardActivity: onCreate
01-29 00:18:52.605  5935  5935 D StandardActivity: onStart: com.bill.activitiesmodedemo.StandardActivity@b6ae6da
01-29 00:18:52.607  5935  5935 D StandardActivity: onResume: com.bill.activitiesmodedemo.StandardActivity@b6ae6da
01-29 00:18:52.714  1416  1445 I ActivityManager: Displayed com.bill.activitiesmodedemo/.StandardActivity: +204ms

//Step 2: SingleTop模式B创建，此时A暂停A->onPause;B->onCreate->onStart->onResume;A->onStop;
01-29 00:18:54.873  5935  5935 D StandardActivity: onPause: com.bill.activitiesmodedemo.StandardActivity@b6ae6da
01-29 00:18:54.960  5935  5935 D SingleTopActivity: onCreate
01-29 00:18:54.964  5935  5935 D SingleTopActivity: onStart: com.bill.activitiesmodedemo.SingleTopActivity@6aa9443
01-29 00:18:54.967  5935  5935 D SingleTopActivity: onResume: com.bill.activitiesmodedemo.SingleTopActivity@6aa9443
01-29 00:18:55.062  1416  1445 I ActivityManager: Displayed com.bill.activitiesmodedemo/.SingleTopActivity: +182ms
01-29 00:18:55.279  5935  5935 D StandardActivity: onStop: com.bill.activitiesmodedemo.StandardActivity@b6ae6da

//Step 3: SingleTask模式C创建,此时B暂停,B->onPause;C->onStart->onStart->onResume;B->onStop
01-29 00:18:57.243  5935  5935 D SingleTopActivity: onPause: com.bill.activitiesmodedemo.SingleTopActivity@6aa9443
01-29 00:18:57.345  5935  5935 D SingleTaskActivity: onStart: com.bill.activitiesmodedemo.SingleTaskActivity@4396698
01-29 00:18:57.347  5935  5935 D SingleTaskActivity: onResume: com.bill.activitiesmodedemo.SingleTaskActivity@4396698
01-29 00:18:57.454  1416  1445 I ActivityManager: Displayed com.bill.activitiesmodedemo/.SingleTaskActivity: +204ms
01-29 00:18:57.692  5935  5935 D SingleTopActivity: onStop: com.bill.activitiesmodedemo.SingleTopActivity@6aa9443

//Step 4: SingleInstance模式D创建，此时C暂停,C->onPause;D->onCreate->onStart->onResume;C->onStop
01-29 00:18:59.734  5935  5935 D SingleTaskActivity: onPause: com.bill.activitiesmodedemo.SingleTaskActivity@4396698
01-29 00:18:59.816  5935  5935 D SingleInstancesActivity: onCreate: com.bill.activitiesmodedemo.SingleInstancesActivity@d4148e9
01-29 00:18:59.820  5935  5935 D SingleInstancesActivity: onStart: com.bill.activitiesmodedemo.SingleInstancesActivity@d4148e9
01-29 00:18:59.822  5935  5935 D SingleInstancesActivity: onResume: com.bill.activitiesmodedemo.SingleInstancesActivity@d4148e9
01-29 00:18:59.926  1416  1445 I ActivityManager: Displayed com.bill.activitiesmodedemo/.SingleInstancesActivity: +182ms
01-29 00:19:00.173  5935  5935 D SingleTaskActivity: onStop: com.bill.activitiesmodedemo.SingleTaskActivity@4396698


//Step 5: Standard模式A创建,此时A创建,且id与之前的b6ae6da不同。D->Pause;A->onCreate->onStart->onResume;D->onStop
01-29 00:19:01.976  5935  5935 D SingleInstancesActivity: onPause: com.bill.activitiesmodedemo.SingleInstancesActivity@d4148e9
01-29 00:19:02.083  5935  5935 D StandardActivity: onCreate
01-29 00:19:02.087  5935  5935 D StandardActivity: onStart: com.bill.activitiesmodedemo.StandardActivity@a7f61e1
01-29 00:19:02.091  5935  5935 D StandardActivity: onResume: com.bill.activitiesmodedemo.StandardActivity@a7f61e1
01-29 00:19:02.189  1416  1445 I ActivityManager: Displayed com.bill.activitiesmodedemo/.StandardActivity: +204ms
01-29 00:19:02.401  5935  5935 D SingleInstancesActivity: onStop: com.bill.activitiesmodedemo.SingleInstancesActivity@d4148e9


//Step 6: SingleTop模式下B创建，由于栈顶不为B,所以生成的id与之前的6aa9443不同，A->onPause;B->onCreate->onStart->onResume;A->onStop
01-29 00:19:04.011  5935  5935 D StandardActivity: onPause: com.bill.activitiesmodedemo.StandardActivity@a7f61e1
01-29 00:19:04.080  5935  5935 D SingleTopActivity: onCreate
01-29 00:19:04.083  5935  5935 D SingleTopActivity: onStart: com.bill.activitiesmodedemo.SingleTopActivity@89559d9
01-29 00:19:04.085  5935  5935 D SingleTopActivity: onResume: com.bill.activitiesmodedemo.SingleTopActivity@89559d9
01-29 00:19:04.197  1416  1445 I ActivityManager: Displayed com.bill.activitiesmodedemo/.SingleTopActivity: +174ms


//Step 7: SingleTask模式下C创建，且id与之前的相同,此时C并没有回调onCreate,而是调用了onNewIntent:B->onPause;C->onNewIntent->onStart->onResume;B->onStop
01-29 00:19:06.084  5935  5935 D SingleTopActivity: onPause: com.bill.activitiesmodedemo.SingleTopActivity@89559d9
01-29 00:19:06.107  5935  5935 D SingleTaskActivity: onNewIntent: com.bill.activitiesmodedemo.SingleTaskActivity@4396698
01-29 00:19:06.110  5935  5935 D SingleTaskActivity: onStart: com.bill.activitiesmodedemo.SingleTaskActivity@4396698
01-29 00:19:06.112  5935  5935 D SingleTaskActivity: onResume: com.bill.activitiesmodedemo.SingleTaskActivity@4396698
01-29 00:19:06.385  5935  5935 D SingleTopActivity: onStop: com.bill.activitiesmodedemo.SingleTopActivity@89559d9

//Step 8: SingleInstance模式下D创建，id也与之前的对象相同，流程为C->onPause;D->onNewIntent->onStart->onResume;C->onStop
01-29 00:19:07.974  5935  5935 D SingleTaskActivity: onPause: com.bill.activitiesmodedemo.SingleTaskActivity@4396698
01-29 00:19:07.987  5935  5935 D SingleInstancesActivity: onNewIntent: com.bill.activitiesmodedemo.SingleInstancesActivity@d4148e9
01-29 00:19:07.989  5935  5935 D SingleInstancesActivity: onStart: com.bill.activitiesmodedemo.SingleInstancesActivity@d4148e9
01-29 00:19:07.991  5935  5935 D SingleInstancesActivity: onResume: com.bill.activitiesmodedemo.SingleInstancesActivity@d4148e9
01-29 00:19:08.427  5935  5935 D SingleTaskActivity: onStop: com.bill.activitiesmodedemo.SingleTaskActivity@4396698
""
```

