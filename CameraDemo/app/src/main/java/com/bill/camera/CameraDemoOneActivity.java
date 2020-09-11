package com.bill.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bill.camera.CameraUtils.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.bill.camera.CameraUtils.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE;
import static com.bill.camera.CameraUtils.MEDIA_TYPE_IMAGE;
import static com.bill.camera.CameraUtils.MEDIA_TYPE_VIDEO;
import static com.bill.camera.CameraUtils.getOutputMediaFileUri;
import static com.bill.camera.CameraUtils.verifyCameraPermissions;
import static com.bill.camera.CameraUtils.verifyStoragePermissions;

/**
 * @author BillCong
 */
public class CameraDemoOneActivity extends AppCompatActivity {
    @BindView(R.id.fullscreen_content)
    TextView fullscreenContent;
    @BindView(R.id.fullscreen_content_controls)
    LinearLayout fullscreenContentControls;
    @BindView(R.id.capture_button)
    Button captureButton;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.record_button)
    Button recordButton;

    Context mContext;
    private Uri fileUri;
    private static String TAG = "CameraDemoOneActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera_demo_one);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mContext = this;
        verifyStoragePermissions(this);
        verifyCameraPermissions(this);
    }


    @OnClick({R.id.capture_button, R.id.record_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.capture_button:
                captureFunc();
                break;
            case R.id.record_button:
                recordFunc();
                break;
        }
    }

    private void captureFunc(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(mContext, MEDIA_TYPE_IMAGE);
        Log.d(TAG, "bill fileUri = " + fileUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void recordFunc(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = getOutputMediaFileUri(mContext, MEDIA_TYPE_VIDEO);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE == requestCode) {
            Log.d(TAG, "resultCode = " + resultCode);
            if (RESULT_OK == resultCode) {
                if (data != null) {
                    Log.d(TAG, "resultCode = " + resultCode + " case 1");
                    Toast.makeText(this, "Image saved to:\n" + data.getData(),
                            Toast.LENGTH_LONG).show();
                    if (data.hasExtra("data")) {
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        imageView.setImageBitmap(thumbnail);
                    }
                } else {
                    Log.d(TAG, "resultCode = " + resultCode + " case 2");
                    int width = imageView.getWidth();
                    int height = imageView.getHeight();
                    BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
                    factoryOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);
                    int imageWidth = factoryOptions.outWidth;
                    int imageHeight = factoryOptions.outHeight;
                    int scaleFactor = Math.min(imageWidth / width, imageHeight
                            / height);
                    factoryOptions.inJustDecodeBounds = false;
                    factoryOptions.inSampleSize = scaleFactor;
                    factoryOptions.inPurgeable = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                            factoryOptions);
                    imageView.setImageBitmap(bitmap);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Result canceled");
            } else {
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            } else if (resultCode == RESULT_CANCELED) {
            } else {
            }
        }
    }
}