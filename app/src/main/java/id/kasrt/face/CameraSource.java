package id.kasrt.face;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;

public class CameraSource {

    public interface CameraSourceCallback {
        void onDetected(Bitmap bitmap);
    }

    private Camera camera;
    private CameraSourceCallback callback;
    private SurfaceHolder surfaceHolder;

    public CameraSource(SurfaceHolder surfaceHolder, CameraSourceCallback callback) {
        this.callback = callback;
        this.surfaceHolder = surfaceHolder;
    }

    public void start() {
        camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        Camera.Size size = params.getSupportedPreviewSizes().get(0);
        params.setPreviewSize(size.width, size.height);
        params.setPreviewFormat(ImageFormat.NV21);
        camera.setParameters(params);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.setPreviewCallback((data, camera) -> {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size previewSize = parameters.getPreviewSize();
            int width = previewSize.width;
            int height = previewSize.height;

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            // Convert data to bitmap (this should be done properly)
            callback.onDetected(bitmap);
        });

        camera.startPreview();
    }

    public void takePicture() {
        camera.takePicture(null, null, (data, camera) -> {
            // Process the picture data if needed
        });
    }

    public void stop() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
