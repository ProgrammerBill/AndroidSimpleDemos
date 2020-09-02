package com.bill.mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author chenjuncong
 */
public class MainActivity extends AppCompatActivity{
    public static String TAG = "MediaPlayerDemo";
    @BindView(R.id.videoViewButton)
    Button videoViewButton;
    @BindView(R.id.mediaCodecButton)
    Button mediaCodecButton;
    @BindView(R.id.mediaPlayerButton)
    Button mediaPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.videoViewButton, R.id.mediaCodecButton, R.id.mediaPlayerButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.videoViewButton:
                startActivity(new Intent(this, VideoViewDemoActivity.class));
                break;
            case R.id.mediaCodecButton:
                startActivity(new Intent(this, MediaCodecDemoActivity.class));
                break;
            case R.id.mediaPlayerButton:
                startActivity(new Intent(this, MediaPlayerDemoActivity.class));
                break;
        }
    }
}