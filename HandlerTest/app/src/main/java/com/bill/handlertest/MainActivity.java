package com.bill.handlertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.ThreadToMainButton)
    Button ThreadToMainButton;
    @BindView(R.id.MainToThreadButton)
    Button MainToThreadButton;
    @BindView(R.id.ThreadToThread)
    Button ThreadToThread;

    private final static  String TAG = "HandlerTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ThreadToMainButton, R.id.MainToThreadButton, R.id.ThreadToThread})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ThreadToMainButton:
                startTestActivity(ThreadToMainActivity.class);
                break;
            case R.id.MainToThreadButton:
                startTestActivity(MainToThreadActivity.class);
                break;
            case R.id.ThreadToThread:
                startTestActivity(ThreadToThreadActivity.class);
                break;
        }
    }

    private void startTestActivity(Class<?> cls){
        Intent mIntent = new Intent(MainActivity.this,
                cls);
        startActivity(mIntent);
    }

    public static String getTAG(){
        return TAG;
    }
}
