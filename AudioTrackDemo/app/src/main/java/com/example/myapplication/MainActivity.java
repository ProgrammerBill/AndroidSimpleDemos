package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * MainActivity class
 *
 * @author billCong
 * @date 2019/4/16
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.playButton)
    Button playButton;
    @BindView(R.id.stopButton)
    Button stopButton;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.guideline2)
    Guideline guideline2;
    public static String TAG = "AudioTrackDemo";
    private String musicPath = null;
    private AudioDecoder mPlayer = null;

    private static final int REQUEST_FILE = 0;
    private static final int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPlayer = new AudioDecoder();
    }

    @OnClick({R.id.playButton, R.id.stopButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                Log.d(TAG, "play button pressed!");
                requestAllPermissions();
                break;
            case R.id.stopButton:
                Log.d(TAG, "stop button pressed!");
                mPlayer.stopPlay();
                break;
        }
    }

    public void requestAllPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Log.d(TAG,"has requested permissions");
                getFile();
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION);
            }
        }
        else{
            Log.d(TAG,"has requested permissions");
            getFile();
        }
    }

    public void getFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE && resultCode == RESULT_OK) {
            Log.d(TAG,"onActivityResult!");
            Uri uri = data.getData();
            musicPath = uri.getPath();
            mPlayer.startPlay(musicPath);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION){
            getFile();
            mPlayer.startPlay(musicPath);
        }
    }
}
