package id.kasrt.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CameraSource extends SurfaceView implements SurfaceHolder.Callback {

    public interface CameraSourceCallback {
        void onDetected(Bitmap bitmap);
    }

    private Camera camera;
    private CameraSourceCallback callback;
    private SurfaceHolder surfaceHolder;
    private Context context;

    public CameraSource(Context context, SurfaceHolder surfaceHolder, CameraSourceCallback callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.surfaceHolder = surfaceHolder;

        this.surfaceHolder.addCallback(this);
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        startCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCamera();
    }

    public void startCamera() {
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            setCameraDisplayOrientation();
            Camera.Parameters params = camera.getParameters();
            Camera.Size size = params.getSupportedPreviewSizes().get(0);
            params.setPreviewSize(size.width, size.height);
            params.setPreviewFormat(ImageFormat.NV21);
            camera.setParameters(params);

            camera.setPreviewDisplay(surfaceHolder);
            camera.setPreviewCallback((data, camera) -> {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size previewSize = parameters.getPreviewSize();
                int width = previewSize.width;
                int height = previewSize.height;

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                // Convert data to bitmap (this should be done properly)
                convertNV21ToBitmap(data, width, height, bitmap);
                callback.onDetected(bitmap);
            });

            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCameraDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, cameraInfo);
        int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (cameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (cameraInfo.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void stopCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void stop() {

        if (camera != null) {
            camera.stopPreview();
        }
    }

    private void convertNV21ToBitmap(byte[] nv21, int width, int height, Bitmap bitmap) {
        // Convert NV21 to ARGB_8888 Bitmap
        // You may need a proper implementation here
        // Here's a placeholder implementation:
        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap newBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, options);
        bitmap.setPixels(new int[width * height], 0, width, 0, 0, width, height);
    }
}
