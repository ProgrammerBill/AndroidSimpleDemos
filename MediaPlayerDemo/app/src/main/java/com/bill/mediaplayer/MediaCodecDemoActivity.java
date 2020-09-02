package com.bill.mediaplayer;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author chenjuncong
 */
public class MediaCodecDemoActivity extends AppCompatActivity implements MediaBase {
    private static String TAG = MainActivity.TAG;
    @BindView(R.id.openButton)
    Button openButton;
    @BindView(R.id.pauseButton)
    Button pauseButton;
    @BindView(R.id.stopButton)
    Button stopButton;
    @BindView(R.id.playButton)
    Button playButton;
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;

    private SurfaceHolder mSurfaceHolder;
    private WorkerThread mWorkerThread;
    private MediaCodec mDecoder;
    private MediaExtractor mExtractor;
    boolean isPaused;
    boolean isStopped;

    private int timeOutUs = 1000;
    private static final int HANDLER_INIT_STATES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_codec_demo);
        ButterKnife.bind(this);
        initSurfaceViewListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initButtonsStates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initButtonsStates(){
        openButton.setEnabled(true);
        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    @Override
    public void openFile() {
        isPaused = false;
        isStopped = false;
        try{
            mWorkerThread = new WorkerThread(mSurfaceHolder);
            if(mExtractor == null){
                mExtractor = new MediaExtractor();
            }
            mExtractor.setDataSource(VIDEO_PATH);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        playButton.setEnabled(true);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    @Override
    public void play() {
        if(!isPaused){
            mWorkerThread.start();
        }
        else{
            isPaused = false;
        }
        openButton.setEnabled(false);
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    @Override
    public void pause() {
        isPaused = true;
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    @Override
    public void stop() {
        isStopped = true;
        playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
    }

    private void initSurfaceViewListener(){
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG,"surfaceCreated!");
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

    @OnClick({R.id.openButton, R.id.pauseButton, R.id.stopButton, R.id.playButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.openButton:
                openFile();
                break;
            case R.id.pauseButton:
                pause();
                break;
            case R.id.stopButton:
                stop();
                break;
            case R.id.playButton:
                play();
                break;
        }
    }

    private class WorkerThread extends Thread {
        SurfaceHolder mSurfaceHolder;
        WorkerThread(SurfaceHolder holder){
            mSurfaceHolder = holder;
        }

        @Override
        public void run() {
            for (int i = 0; i < mExtractor.getTrackCount(); i++) {
                MediaFormat format = mExtractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (mime.startsWith("video/")) {
                    mExtractor.selectTrack(i);
                    try {
                        mDecoder = MediaCodec.createDecoderByType(mime);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mDecoder.configure(format, mSurfaceHolder.getSurface(), null, 0);
                    break;
                }
            }

            mDecoder.start();
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            boolean isEOS = false;

            while (!Thread.interrupted() && !isStopped) {
                if(isPaused){
                    try {
                        sleep(timeOutUs);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (!isEOS) {
                    int inIndex = mDecoder.dequeueInputBuffer(timeOutUs);
                    if (inIndex >= 0) {
                        ByteBuffer buffer = mDecoder.getInputBuffer(inIndex);
                        int sampleSize = mExtractor.readSampleData(buffer, 0);
                        if (sampleSize < 0) {
                            mDecoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            isEOS = true;
                        } else {
                            mDecoder.queueInputBuffer(inIndex, 0, sampleSize, mExtractor.getSampleTime(), 0);
                            mExtractor.advance();
                        }
                    }
                }

                int outIndex = mDecoder.dequeueOutputBuffer(info, timeOutUs);
                if(outIndex >= 0){
                    ByteBuffer buffer = mDecoder.getOutputBuffer(outIndex);
                    mDecoder.releaseOutputBuffer(outIndex, true);
                }

                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
            }
            mDecoder.stop();
            mDecoder.release();
            mDecoder = null;
            mExtractor.release();
            mExtractor = null;
            Message msg = mHandler.obtainMessage();
            msg.what = HANDLER_INIT_STATES;
            mHandler.sendMessage(msg);
        }

        private Handler mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case HANDLER_INIT_STATES:
                        initButtonsStates();
                        break;
                }
            }
        };
    }
}