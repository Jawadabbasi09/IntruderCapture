package com.example.intrudercapture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import kotlinx.coroutines.DebugKt;

public class CameraManager implements Camera.PictureCallback, Camera.ErrorCallback, Camera.PreviewCallback, Camera.AutoFocusCallback {
    private static CameraManager mManager;
    private Camera mCamera;
    private Context mContext;
    private SurfaceTexture mSurface;

    public CameraManager(Context context) {
        this.mContext = context;
    }

    public static CameraManager getInstance(Context context) {
        if (mManager == null) {
            mManager = new CameraManager(context);
        }
        return mManager;
    }

    public void takePhoto() {
        if (isFrontCameraAvailable()) {
            initCamera();
        }
    }

    private boolean isFrontCameraAvailable() {
        Camera.CameraInfo cameraInfo = null;
        Context context = this.mContext;
        if (context == null || !context.getPackageManager().hasSystemFeature("android.hardware.camera")) {
            return false;
        }
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            try {
                cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            if (cameraInfo.facing == 1) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.foxbytecode.captureintruder.manager.CameraManager$1] */
    @SuppressLint("StaticFieldLeak")
    private void initCamera() {
        Log.e("INTIAL", "camera");
        new AsyncTask() { // from class: com.foxbytecode.captureintruder.manager.CameraManager.1
            @Override // android.os.AsyncTask
            protected Object doInBackground(Object[] objArr) {
                try {
                    Log.e("doinback", "camera");
                    CameraManager.this.mCamera = Camera.open(1);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override // android.os.AsyncTask
            protected void onPostExecute(Object obj) {
                Log.e("postexe", "camera");
                try {
                    if (CameraManager.this.mCamera != null) {
                        CameraManager.this.mSurface = new SurfaceTexture(123);
                        CameraManager.this.mCamera.setPreviewTexture(CameraManager.this.mSurface);
                        try {
                            Camera.Parameters parameters = CameraManager.this.mCamera.getParameters();
                            parameters.setRotation(270);
                            CameraManager cameraManager = CameraManager.this;
                            if (cameraManager.autoFocusSupported(cameraManager.mCamera)) {
                                parameters.setFocusMode(DebugKt.DEBUG_PROPERTY_VALUE_AUTO);
                            } else {
                                Log.w("asdaxxx", "Autofocus is not supported");
                            }
                            try {
                                CameraManager.this.mCamera.setParameters(parameters);
                                CameraManager.this.mCamera.setPreviewCallback(CameraManager.this);
                                CameraManager.this.mCamera.setErrorCallback(CameraManager.this);
                                CameraManager.this.mCamera.startPreview();
                                return;
                            } catch (Exception e) {
                                return;
                            }
                        } catch (RuntimeException e2) {
                            e2.printStackTrace();
                            return;
                        }
                    }
                    CameraManager.getInstance(CameraManager.this.mContext).takePhoto();
                } catch (IOException e3) {
                    Log.e("exeception", "camera");
                    e3.printStackTrace();
                    CameraManager.this.releaseCamera();
                }
            }
        }.execute(new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean autoFocusSupported(Camera camera) {
        if (camera != null) {
            try {
                return camera.getParameters().getSupportedFocusModes().contains(DebugKt.DEBUG_PROPERTY_VALUE_AUTO);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseCamera() {
        Camera camera = this.mCamera;
        if (camera != null) {
            camera.release();
            this.mSurface.release();
            this.mCamera = null;
            this.mSurface = null;
        }
    }

    @Override // android.hardware.Camera.ErrorCallback
    public void onError(int i, Camera camera) {
        if (i == 1) {
            Log.e("ContentValues", "Camera error: Unknown");
        } else if (i == 2) {
            Log.e("ContentValues", "Camera error: Camera was disconnected due to use by higher priority user");
        } else if (i == 100) {
            Log.e("ContentValues", "Camera error: Media server died");
        } else {
            Log.e("ContentValues", "Camera error: no such error id (" + i + ")");
        }
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        try {
            if (autoFocusSupported(camera)) {
                camera.setPreviewCallback(null);
                camera.takePicture(null, null, this);
            } else {
                camera.setPreviewCallback(null);
                camera.takePicture(null, null, this);
            }
        } catch (Exception e) {
            Log.e("ContentValues", "Camera error while taking picture");
            e.printStackTrace();
            releaseCamera();
        }
    }

    @Override // android.hardware.Camera.AutoFocusCallback
    public void onAutoFocus(boolean z, Camera camera) {
        if (camera != null) {
            try {
                camera.takePicture(null, null, this);
                this.mCamera.autoFocus(null);
            } catch (Exception e) {
                e.printStackTrace();
                releaseCamera();
            }
        }
    }

    @Override // android.hardware.Camera.PictureCallback
    public void onPictureTaken(byte[] bArr, Camera camera) {
        Bitmap btmp=byteArrayToBitmap(bArr,50);
//        savePicture(bArr);
        saveImages(btmp);

        Log.d("array", bArr.length+"");
        releaseCamera();
    }
    public void saveImages(Bitmap bmp) {
        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/intruder");

        if (!direct.exists()) {
            File imageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    + "/intruder");
            imageDirectory.mkdirs();
        }

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fileName = "Image-" + n + ".png";
        File file = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/intruder"), fileName);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            refreshGallery(mContext,file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap byteArrayToBitmap(byte[] byteArray, int compressionQuality) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Compress the bitmap with the specified quality and save it to the output stream
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, outputStream);

        // Convert the output stream back to a bitmap
        byte[] compressedByteArray = outputStream.toByteArray();
        return BitmapFactory.decodeByteArray(compressedByteArray, 0, compressedByteArray.length);
    }

    // Example usage:
    private String savePicture(byte[] bArr) {
        try {
            File dir = getDir();
            if (bArr == null) {
                Toast.makeText(this.mContext, "cant save image", Toast.LENGTH_LONG).show();
                Log.e("asdaxxx", "Can't save image - no data");
                return null;
            } else if (!dir.exists() && !dir.mkdirs()) {
                Toast.makeText(this.mContext, "Can't create directory to save image", Toast.LENGTH_LONG).show();
                Log.e("asdaxxx", "Can't create directory to save image.");
                return null;
            } else {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String str = "IntruderCapture_" + String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) + simpleDateFormat.format(new Date()) + ".jpg";
                String str2 = dir.getPath() + File.separator + str;
                Log.d("asdaxxx", str2);
                FileOutputStream fileOutputStream = new FileOutputStream(new File(str2));
                fileOutputStream.write(bArr);
                fileOutputStream.close();
                Log.d("asdaxxx", "New image was saved" + str);
                return str2;
            }
        } catch (Exception e) {
            Log.e("asdaxxx", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private File getDir() {
        return new File(this.mContext.getFilesDir(), "IntruderCapure");
    }


        public  void refreshGallery(Context context, String filePath) {
            MediaScannerConnection.scanFile(context, new String[]{filePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, android.net.Uri uri) {
                    // Scanning complete, you can perform any desired operations here
                }
            });
        }

}