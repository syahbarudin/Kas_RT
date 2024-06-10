package id.kasrt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import id.kasrt.databinding.ActivityQrScannerBinding

class QrScannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Periksa apakah aplikasi dibuka melalui hasil pemindaian QR code
        val data: Uri? = intent?.data
        if (data != null) {
            // Jika aplikasi dibuka melalui hasil pemindaian QR code, langsung buka tautan
            openUrl(data)
        } else {
            // Jika tidak, inisialisasi QR Scanner secara otomatis saat Activity dibuka
            startQrScanner()
        }
    }

    private fun startQrScanner() {
        IntentIntegrator(this).apply {
            setPrompt("Arahkan kamera ke QR code")
            setCameraId(0) // Kamera belakang
            setOrientationLocked(true)
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
            setCaptureActivity(CustomCaptureActivity::class.java)
            initiateScan()
        }
    }

    private fun openUrl(url: Uri) {
        // Buka tautan yang ditemukan
        val intent = Intent(Intent.ACTION_VIEW, url)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan dibatalkan", Toast.LENGTH_SHORT).show()
            } else {
                handleQrResult(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleQrResult(result: String) {
        if (result.startsWith("http://") || result.startsWith("https://")) {
            // Jika hasil QR code adalah tautan, langsung buka tautan
            openUrl(Uri.parse(result))
        } else {
            // Jika tidak, tampilkan hasil QR code
            Toast.makeText(this, "Hasil QR: $result", Toast.LENGTH_SHORT).show()
        }
        // Restart QR Scanner after showing the result
        startQrScanner()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Kembali langsung ke halaman beranda (MenuActivity)
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
