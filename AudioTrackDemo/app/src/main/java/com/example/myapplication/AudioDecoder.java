package com.example.myapplication;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AudioDecoder {

    /**
    * 采样率
    */
    final int TEST_SR = 44000;
    /**
     * 声道数
     * */
    final int TEST_CONF = AudioFormat.CHANNEL_OUT_STEREO;
    /**
     * 采样精度
     * */
    final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    /**
     * 数据加载类型,STREAM或者STATIC
     * */
    final int TEST_MODE = AudioTrack.MODE_STREAM;
    /**
     * 音频流类型
     * */
    final int TEST_STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private static String mPath = "";

    private AudioTrack mAudioTrack = null;
    private byte []audioData = null;
    private int minBuffSize;

    Runnable playRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(MainActivity.TAG,"");
            playAudioTrack();
        }
    };

    public void startPlay(String path) {
        mPath = path;
        initAudioData();
        new Thread(playRunnable).start();
    }

    private void initAudioData(){
        //获取分配缓冲区的最小buffer大小
        minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
        //新建AudioTrack
        mAudioTrack = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT, minBuffSize, TEST_MODE);
        audioData = new byte[minBuffSize/2];
    }

    private void playAudioTrack(){
        mAudioTrack.play();
        InputStream in = null;
        try{
            in = new FileInputStream(mPath);
            int n = 0;
            while((n = in.read(audioData)) != -1){
                mAudioTrack.write(audioData, 0, audioData.length);
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if(in != null){
                    in.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void stopPlay(){
        mAudioTrack.stop();
    }
}
