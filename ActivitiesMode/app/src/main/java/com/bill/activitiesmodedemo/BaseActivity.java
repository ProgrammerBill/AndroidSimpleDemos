package com.bill.activitiesmodedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.standard:
                mIntent = new Intent(this,
                        StandardActivity.class);
                break;
            case R.id.singleTask:
                mIntent = new Intent(this,
                        SingleTaskActivity.class);
                break;
            case R.id.singleTop:
                mIntent = new Intent(this,
                        SingleTopActivity.class);
                break;
            case R.id.singleInstance:
                mIntent = new Intent(this,
                        SingleInstancesActivity.class);
                break;
        }
        startActivity(mIntent);
    }
}
