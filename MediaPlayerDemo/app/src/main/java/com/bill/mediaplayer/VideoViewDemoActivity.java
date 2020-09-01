package com.bill.mediaplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author chenjuncong
 */
public class VideoViewDemoActivity extends AppCompatActivity {
    private static String TAG = MainActivity.TAG;
    private static String VIDEO_PATH = "/data/test.mp4";
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.openButton)
    Button openButton;
    @BindView(R.id.stopButton)
    Button stopButton;
    @BindView(R.id.pauseButton)
    Button pauseButton;
    @BindView(R.id.playButton)
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_view_demo);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.setMediaController(new MediaController(this));
    }


    private void openFile() {
        Log.d(TAG, "openFile");
        videoView.setVideoPath(VIDEO_PATH);
        videoView.resume();
    }

    private void play() {
        Log.d(TAG, "play");
        videoView.start();
    }

    private void pause() {
        Log.d(TAG, "pause");
        if (videoView.canPause()) {
            videoView.pause();
        }
    }

    private void stop() {
        Log.d(TAG, "stop");
        videoView.stopPlayback();
    }

    @OnClick({R.id.openButton, R.id.stopButton, R.id.pauseButton, R.id.playButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.openButton:
                openFile();
                break;
            case R.id.stopButton:
                stop();
                break;
            case R.id.pauseButton:
                pause();
                break;
            case R.id.playButton:
                play();
                break;
        }
    }
}