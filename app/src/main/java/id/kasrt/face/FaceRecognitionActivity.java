package id.kasrt.face;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import id.kasrt.LoginActivity;
import id.kasrt.R;
import id.kasrt.ui.fragmentmain;

public class FaceRecognitionActivity extends AppCompatActivity implements CameraSource.CameraSourceCallback {

    private SurfaceView cameraPreview;
    private TextView tvStatus;
    private CameraSource cameraSource;
    private String actionType;

    private static final String TAG = "FaceRecognitionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        cameraPreview = findViewById(R.id.camera_preview);
        tvStatus = findViewById(R.id.tv_status);

        FrameLayout cameraContainer = findViewById(R.id.camera_container);
        cameraContainer.setOutlineProvider(new CircleOutlineProvider());
        cameraContainer.setClipToOutline(true);

        actionType = getIntent().getStringExtra("ACTION_TYPE");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            startCameraSource();
        }
    }

    private void startCameraSource() {
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, cameraPreview.getHolder(), this);
        }
        cameraSource.startCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCameraSource();
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetected(Bitmap bitmap) {
        Log.d(TAG, "onDetected: Bitmap detected");

        if (bitmap == null) {
            Log.e(TAG, "onDetected: Bitmap is null");
            tvStatus.setText("Failed to capture image, please try again.");
            return;
        }

        InputImage image = InputImage.fromBitmap(bitmap, 0); // adjust rotation as needed
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();

        FaceDetector detector = FaceDetection.getClient(options);

        detector.process(image)
                .addOnSuccessListener(faces -> {
                    Log.d(TAG, "onDetected: Faces detected: " + faces.size());
                    if (faces.size() > 0) {
                        Face face = faces.get(0);
                        if ("REGISTER".equals(actionType)) {
                            registerFace(face);
                        } else {
                            loginFace(face);
                        }
                    } else {
                        tvStatus.setText("No face detected, please try again.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onDetected: Face detection failed", e);
                    tvStatus.setText("Failed to detect face, please try again.");
                });
    }

    private void registerFace(Face face) {
        // Simpan data wajah ke Firebase Firestore atau basis data lokal
        tvStatus.setText("Face registered successfully.");
    }

    private void loginFace(Face face) {
        // Implementasi login dengan data wajah yang terdeteksi
        // Misalnya, Anda bisa mencocokkan data ini dengan data pengguna yang tersimpan di Firebase Firestore
        tvStatus.setText("Face recognized, logging in...");
        startActivity(new Intent(this, fragmentmain.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Resuming activity");
        if (cameraSource != null) {
            cameraSource.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Pausing activity");
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Destroying activity");
        if (cameraSource != null) {
            cameraSource.stopCamera();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Navigasi ke LoginActivity ketika tombol kembali ditekan
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();  // Menghindari kembali ke FaceRecognitionActivity saat kembali dari LoginActivity
    }
}
