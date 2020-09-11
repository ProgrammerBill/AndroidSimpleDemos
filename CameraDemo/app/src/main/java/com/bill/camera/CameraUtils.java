package com.bill.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author BillCong
 */
public class CameraUtils {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CAMERA = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static String[] PERMISSIONS_CAMERA = {
            "android.permission.CAMERA"};

    public static Uri getOutputMediaFileUri(Context context, int type)
    {
        Uri imageUri = FileProvider.getUriForFile(
                context,
                "com.bill.myapplication.provider",
                getOutputMediaFile(context, type));
        return imageUri;
    }

    public static File getOutputMediaFile(Context context, int type)
    {
        File mediaStorageDir = null;
        try
        {
            //mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            mediaStorageDir = new File("/sdcard/Pictures");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
            try{
                mediaFile.createNewFile();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else if (type == MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
            try{
                mediaFile.createNewFile();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            return null;
        }

        return mediaFile;
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void verifyCameraPermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.CAMERA");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMERA, REQUEST_CAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
