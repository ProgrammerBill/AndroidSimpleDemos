package com.example.jnidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.jnidemo.NdkUtils.NdkUtilsBase;
import com.example.jnidemo.NdkUtils.NdkUtilsDynamic;
import com.example.jnidemo.NdkUtils.NdkUtilsStatic;
import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Demo class
 *
 * @author Bill
 * @date 2019/02/20
 */
public class MainActivity extends AppCompatActivity {


    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.getTime)
    Button getTime;
    @BindView(R.id.jniMode)
    CheckBox jniMode;

    private NdkUtilsBase mNdkUtils;
    private final String TAG = "JniDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNdkUtils = new NdkUtilsStatic();
        ButterKnife.bind(this);
    }


    @OnClick({R.id.getTime, R.id.jniMode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getTime:
                String currentTime = mNdkUtils.getTime();
                textView.setText(currentTime);
                break;
            case R.id.jniMode:
                CheckBox tmp = (CheckBox)view;
                if(tmp.isChecked()){
                    Log.d(TAG,"use Dynamic mode!");
                    mNdkUtils = new NdkUtilsDynamic();
                }
                else{
                    Log.d(TAG,"use Static mode!");
                    mNdkUtils = new NdkUtilsStatic();
                }
                break;
        }
    }
}
