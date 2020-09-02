package com.bill.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author chenjuncong
 */
public class MediaPlayerDemoActivity extends AppCompatActivity implements MediaBase {
    private static String TAG = MainActivity.TAG;
    @BindView(R.id.openButton)
    Button openButton;
    @BindView(R.id.stopButton)
    Button stopButton;
    @BindView(R.id.pauseButton)
    Button pauseButton;
    @BindView(R.id.playButton)
    Button playButton;
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;

    MediaPlayer mMediaPlayer;
    SurfaceHolder mSurfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media_player_demo);
        ButterKnife.bind(this);
        mMediaPlayer = new MediaPlayer();
        initSurfaceViewListener();
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

    @Override
    public void openFile(){
        try {
            mMediaPlayer.setDataSource(VIDEO_PATH);
            mMediaPlayer.prepareAsync();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void play(){
        mMediaPlayer.start();
    }

    @Override
    public void pause(){
        mMediaPlayer.pause();
    }

    @Override
    public void stop(){
        mMediaPlayer.stop();
    }

    private void initSurfaceViewListener(){
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG,"surfaceCreated!");
                mMediaPlayer.setDisplay(mSurfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d(TAG,"surfaceChanged!");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG,"surfaceDestroyed!");
            }
        });
    }
}