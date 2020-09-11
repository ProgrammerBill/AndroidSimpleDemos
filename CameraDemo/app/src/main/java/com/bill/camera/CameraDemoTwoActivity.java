package com.bill.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author BillCong
 */
public class CameraDemoTwoActivity extends AppCompatActivity {

    @BindView(R.id.capture_button)
    Button captureButton;
    @BindView(R.id.record_button)
    Button recordButton;
    @BindView(R.id.textureView)
    TextureView textureView;

    private static String TAG = "CameraDemoTwoActivity";
    private CameraManager mCameraManager;
    private String mCameraId;
    private CameraCharacteristics mCharacteristics;
    private Size previewSize;
    private Size outputSize;
    private CameraDevice mCameraDevice;
    private Surface mSurface;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest.Builder mBuilder;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private File mFile;
    private ImageReader mImageReader;
    private int mSensorOrientation;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.v(TAG, "onSurfaceTextureAvailable");
            try {
                setupCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.v(TAG, "onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.d(TAG, "onSurfaceTextureDestroyed");
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.d(TAG,"onOpened");
            mCameraDevice = camera;
            try {
                createPreviewSession();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.d(TAG,"onDisconnected");
            releaseCamera();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.d(TAG,"onError");
            releaseCamera();
        }
    };

    CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            Log.d(TAG,"onConfigured");
            try {
                mBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mBuilder.addTarget(mSurface);
                CaptureRequest request = mBuilder.build();
                session.setRepeatingRequest(request, mCaptureCallback, mBackgroundHandler);
                mCameraCaptureSession = session;

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.d(TAG,"onConfigureFailed");
        }
    };

    CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
            Log.d(TAG,"onCaptureProgressed");
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }
    };

    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_demo_two);
        ButterKnife.bind(this);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        mFile = new File(getExternalFilesDir(null), "pic.jpg");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        textureView.setSurfaceTextureListener(mSurfaceTextureListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick({R.id.capture_button, R.id.record_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.capture_button:
                try {
                    final CaptureRequest.Builder captureBuilder =
                            mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                    captureBuilder.addTarget(mImageReader.getSurface());
                    captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                    int rotation = getWindowManager().getDefaultDisplay().getRotation();
                    captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

                    CameraCaptureSession.CaptureCallback CaptureCallback
                            = new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                       @NonNull CaptureRequest request,
                                                       @NonNull TotalCaptureResult result) {
                            Log.d(TAG, mFile.toString());
                        }
                    };

                    mCameraCaptureSession.stopRepeating();
                    mCameraCaptureSession.abortCaptures();
                    mCameraCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
                }
                catch(CameraAccessException e){
                    e.printStackTrace();
                }
                break;
            case R.id.record_button:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private int getOrientation(int rotation) {
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    private void setupCamera() throws CameraAccessException {
        for (String id : mCameraManager.getCameraIdList()) {
            mCharacteristics = mCameraManager.getCameraCharacteristics(id);
            int facing = mCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                mCameraId = id;
                mSensorOrientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                Log.d(TAG,"mCameraId = " + mCameraId + " orientation = " + mSensorOrientation);
            }
        }

        StreamConfigurationMap map =  mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Size[] sizes = map.getOutputSizes(SurfaceTexture.class);
        List<Size> previewSizes = new ArrayList<>();
        for(Size size: sizes){
            previewSizes.add(size);
        }

        if(previewSizes.size() > 0){
            previewSize = previewSizes.get(0);
        }

        sizes = map.getOutputSizes(ImageFormat.JPEG);
        List<Size> OutputSizes = new ArrayList<>();
        for(Size size: sizes){
            OutputSizes.add(size);
        }

        if(OutputSizes.size() > 0){
            outputSize = OutputSizes.get(0);
        }

        mImageReader = ImageReader.newInstance(outputSize.getWidth(), outputSize.getHeight(),
                ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(
                mOnImageAvailableListener, mBackgroundHandler);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
            return;
        }
        mCameraManager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        configureTransform(previewSize.getWidth(), previewSize.getHeight());
    }

    private void releaseCamera(){
        if(mCameraDevice != null){
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if(mCameraCaptureSession != null){
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createPreviewSession() throws CameraAccessException {
        SurfaceTexture mSurfaceTexture = textureView.getSurfaceTexture();
        mSurfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        mSurface = new Surface(mSurfaceTexture);
        mCameraDevice.createCaptureSession(Arrays.asList(mSurface, mImageReader.getSurface()), mSessionStateCallback, mBackgroundHandler);
    }


    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == textureView || null == previewSize) {
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Log.d(TAG,"rotation = " + rotation);
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / previewSize.getHeight(),
                    (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }

    private static class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            Log.d(TAG,"ImageSaver run!");
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}