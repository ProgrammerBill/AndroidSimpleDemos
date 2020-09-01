package com.bill.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG = "MediaPlayerDemo";
    private Button mVideoViewDemoButton;
    private Button mMediaPlayerDemoButton;
    private Button mMediaCodecDemoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private  void initUI(){
        mVideoViewDemoButton = findViewById(R.id.videoViewButton);
        mVideoViewDemoButton.setOnClickListener(this);
        mMediaPlayerDemoButton = findViewById(R.id.mediaPlayerButton);
        mMediaPlayerDemoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.videoViewButton:
                startActivity(new Intent(this, VideoViewDemoActivity.class));
                break;
            case R.id.mediaPlayerButton:
                startActivity(new Intent(this, MediaPlayerDemoActivity.class));
                break;
            case R.id.mediaCodecButton:
                startActivity(new Intent(this, MediaCodecDemoActivity.class));
                break;
        }
    }
}