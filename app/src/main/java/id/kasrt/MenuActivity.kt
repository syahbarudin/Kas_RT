package id.kasrt

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_activity) // Perbaikan penulisan nama file layout

        // Menghilangkan status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

    }
    fun onMenu1Clicked(view: View) {
        // Kode yang akan dijalankan ketika menu 1 diklik
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onMenu2Clicked(view: View) {
        // Kode yang akan dijalankan ketika menu 2 diklik
        val intent = Intent(this, LaporanActivity::class.java)
        startActivity(intent)
    }

    fun onMenu3Clicked(view: View) {
        // Kode yang akan dijalankan ketika menu 2 diklik
        val intent = Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }

    fun onQrScannerClicked(view: View) {
        val intent = Intent(this, QrScannerActivity::class.java)
        startActivity(intent)
    }
    fun onChatClicked(view: View) {
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
