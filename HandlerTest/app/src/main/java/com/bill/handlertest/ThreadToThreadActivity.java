package com.bill.handlertest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static android.os.Process.myPid;
import static android.os.Process.myTid;

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
