package com.bill.activitiesmodedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity{

    private Button mButton_standard;
    private Button mButton_singleTask;
    private Button mButton_singleTop;
    private Button mButton_singleInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton_standard = findViewById(R.id.standard);
        mButton_singleTask = findViewById(R.id.singleTask);
        mButton_singleTop= findViewById(R.id.singleTop);
        mButton_singleInstance = findViewById(R.id.singleInstance);

        mButton_standard.setOnClickListener(this);
        mButton_singleTask.setOnClickListener(this);
        mButton_singleTop.setOnClickListener(this);
        mButton_singleInstance.setOnClickListener(this);
    }
}
