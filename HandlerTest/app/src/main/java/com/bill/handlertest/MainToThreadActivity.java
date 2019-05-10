package com.bill.handlertest;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static android.os.Process.myPid;
import static android.os.Process.myTid;

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
