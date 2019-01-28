package com.bill.activitiesmodedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SingleTopActivity extends BaseActivity{

    private static String TAG = "SingleTopActivity";

    private Button mButton_standard;
    private Button mButton_singleTask;
    private Button mButton_singleTop;
    private Button mButton_singleInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_top);

        Log.d(TAG,"onCreate");

        mButton_standard = findViewById(R.id.standard);
        mButton_singleTask = findViewById(R.id.singleTask);
        mButton_singleTop= findViewById(R.id.singleTop);
        mButton_singleInstance = findViewById(R.id.singleInstance);

        mButton_standard.setOnClickListener(this);
        mButton_singleTask.setOnClickListener(this);
        mButton_singleTop.setOnClickListener(this);
        mButton_singleInstance.setOnClickListener(this);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart: " + this.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume: " + this.toString());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"onNewIntent: " + this.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause: " + this.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop: " + this.toString());
    }

}
