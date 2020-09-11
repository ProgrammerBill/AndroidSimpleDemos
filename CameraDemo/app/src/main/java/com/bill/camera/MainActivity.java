package com.bill.camera;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Demo class
 *
 * @author BillCong
 * @date 2020/9/9
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "CameraDemo";
    @BindView(R.id.DemoOneButton)
    Button DemoOneButton;
    @BindView(R.id.DemoTwoButton)
    Button DemoTwoButton;

    private Button takePicBtn = null;
    private Button takeVideoBtn = null;
    private ImageView imageView = null;
    private Uri fileUri;
    private Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    @OnClick({R.id.DemoOneButton, R.id.DemoTwoButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.DemoOneButton:
                startActivity(new Intent(this, CameraDemoOneActivity.class));
                break;
            case R.id.DemoTwoButton:
                startActivity(new Intent(this, CameraDemoTwoActivity.class));
                break;
        }
    }
}
