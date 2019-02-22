package com.example.jnidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.jnidemo.NdkUtils.NdkUtilsBase;
import com.example.jnidemo.NdkUtils.NdkUtilsDynamic;
import com.example.jnidemo.NdkUtils.NdkUtilsStatic;
import com.example.jnidemo.R;


/**
 * Demo class
 *
 * @author Bill
 * @date 2019/02/20
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private Button mGetTimeButton;
    private TextView mTimeText;
    private CheckBox mCheckBox;
    private NdkUtilsBase mNdkUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
        mNdkUtils = new NdkUtilsStatic();
    }

    void initViews(){
        mGetTimeButton = (Button)findViewById(R.id.getTime);
        mTimeText = (TextView)findViewById(R.id.textView);
        mCheckBox = (CheckBox)findViewById(R.id.jniMode);
    }

    void initListeners(){
        mGetTimeButton.setOnClickListener(this);
        mCheckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getTime:
                String currentTime = mNdkUtils.getTime();
                mTimeText.setText(currentTime);
                break;
            default:break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.jniMode:
                if(isChecked){
                    mNdkUtils = new NdkUtilsDynamic();
                }
                else{
                    mNdkUtils = new NdkUtilsStatic();
                }
                break;
        }
    }
}
