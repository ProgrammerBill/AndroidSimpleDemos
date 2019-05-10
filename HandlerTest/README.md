# 1. 背景

近日在复习Handler的过程中，萌生出写demo的想法，于是把以前的测试又拿出来重新修改了下，旨在弄清楚Handler与主次线程的关系。

一般handler的创建流程如下:

```java
Looper.prepare();
new Handler();
Looper.loop();
```

1. Looper.prepare()会创建新的Looper，并将其放在线程的私有空间sThreadLocal中，虽然线程可以共享进程的资源，但是该私有资源只有线程本身才能够获得。Looper创建时，会创建唯一一个MessageQueue(消息队列)。
2. 创建Handler，Handler是一个辅助类，可以协助用户发送信息到消息队列，处理信息等。 其构造方法有多个，默认不传Looper的实现，会在当前线程中的sTreadLocal私有空间中获取Looper，此时如果通过该Handler发送消息时，该Handler将会在默认的Looper中处理，即在UI线程中处理。也可以在创建时传入线程的Looper，或者是利用HandlerThread的looper传入，那么Handler的处理消息就会在Looper所在`的线程中处理。(HandlerThread是一个带有Looper的线程，并在run中实现了Loope的循环)
3. Looper.loop()负责循环处理MessageQueu列的信息，假如没有消息将进行阻塞。Looper的底层是使用epoll机制来获取输入信息的。

# 2. Thread->Main Thread

点击ThreadToMain按键后，ThreadToMainActivity创建一个Handler，但此时仍然是主线程，然后再创建一个Thread发送信息，关键代码如下:

```java
public class ThreadToMainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.getTAG();
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 15:
                    Bundle receive = msg.getData();
                    Toast.makeText(ThreadToMainActivity.this,receive.getString("data"),
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Receive Message by Main Thread id:" + myTid() + " pid = " + myPid());
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_to_main);

        new Thread(){
            @Override
            public void run() {
                Message message = new Message();
                message.what = 15;
                final Bundle b = new Bundle();
                b.putString("data","Hi this is sent from thread");
                message.setData(b);
                mHandler.sendMessage(message);
                Log.d(TAG, "send Message by Thread id:" + myTid() + " pid = " + myPid());
            }
        }.start();
    }
}
```

运行的结果如下，发送的线程与主线程不同，Handler处于主线程中。
```
05-10 14:46:18.693 12261 12361 D HandlerTest: send Message by Thread id:12361 pid = 12261
05-10 14:46:18.761 12261 12261 D HandlerTest: Receive Message by Main Thread id:12261 pid = 12261
```

# 3. Main Thread->Thread

```java
public class MainToThreadActivity extends AppCompatActivity {
    private final String TAG = MainActivity.getTAG();
    private Handler mHandler;
    private MyThread mTh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_to_thread);

        mTh = new MyThread();
        mTh.start();
        //make sure looper is exists
        while(mTh.myLooper == null){}
        //mHandler is working in mTh!
        mHandler = new Handler(mTh.myLooper){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Log.d(TAG, "Receive Message by Thread id:" + myTid() + " pid = " + myPid());
                        break;
                    default:
                        break;
                }
            }
        };

        mHandler.sendEmptyMessage(1);
        Log.d(TAG, "send Message by Main Thread id:" + myTid() + " pid = " + myPid());
    }

    class MyThread extends Thread{
        private Looper myLooper;

        @Override
        public void run() {
            Looper.prepare();
            myLooper = Looper.myLooper();
            Looper.loop();
        }
    }
}
```

运行的结果如下，发送的线程是在主线程中，Handler处于子线程中。
```
05-10 15:11:34.115 14490 14490 D HandlerTest: send Message by Main Thread id:14490 pid = 14490
05-10 15:11:34.115 14490 14572 D HandlerTest: Receive Message by Thread id:14572 pid = 14490
```


当然也可以使用HandlerThread去获取looper，那么就不需要通过while等待Thread的looper创建完毕后再往下运行了。

```java
public class MainToThreadActivity extends AppCompatActivity {
    private final String TAG = MainActivity.getTAG();
    private Handler mHandler;
    private HandlerThread mTh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_to_thread);
        mTh = new HandlerThread("thread-handler");
        mTh.start();
        mHandler = new Handler(mTh.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Log.d(TAG, "Receive Message by Thread id:" + myTid() + " pid = " + myPid());
                        break;
                    default:
                        break;
                }
            }
        };

        mHandler.sendEmptyMessage(1);
        Log.d(TAG, "send Message by Main Thread id:" + myTid() + " pid = " + myPid());
    }
}
```


```
05-10 15:15:41.419 14855 14855 D HandlerTest: send Message by Main Thread id:14855 pid = 14855
05-10 15:15:41.419 14855 14976 D HandlerTest: Receive Message by Thread id:14976 pid = 14855
```


# 4. Thread->Thread


```java
public class ThreadToThreadActivity extends AppCompatActivity {
    private final String TAG = MainActivity.getTAG();
    Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_to_thread);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                myHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.d(TAG, "Receive Message by Thread id:" + myTid() + " pid = " + myPid());
                    }
                };
                Looper.loop();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.obj = "Thread 2";
                while(myHandler == null){}
                myHandler.sendMessage(msg);
                Log.d(TAG, "send Message by Thread id:" + myTid() + " pid = " + myPid());
            }
        }).start();
    }
}
```

运行结果看，发送在一个子线程中，接收在另一个子线程中:

```
05-10 15:59:50.064 20011 20124 D HandlerTest: send Message by Thread id:20124 pid = 20011
05-10 15:59:50.064 20011 20123 D HandlerTest: Receive Message by Thread id:20123 pid = 20011
```

