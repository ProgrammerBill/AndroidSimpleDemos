package com.bill.handlertest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static android.os.Process.myPid;
import static android.os.Process.myTid;

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
