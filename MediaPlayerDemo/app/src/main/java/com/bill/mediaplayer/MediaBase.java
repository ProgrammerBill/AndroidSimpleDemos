package com.bill.mediaplayer;

import android.util.Log;

/**
 * @author chenjuncong
 */
public interface MediaBase {
    public static String VIDEO_PATH = "/data/test.mp4";
    /**
     * openFile and reset
     */
    public void openFile();
    /**
     * start playing video
     */
    public void play();
    /**
     * pause playing video
     */
    public void pause();
    /**
     * stop playing video
     */
    public void stop();
}
