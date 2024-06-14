package id.kasrt

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_activity)

        // Menghilangkan status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    fun onMenu1Clicked(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onMenu2Clicked(view: View) {
        val intent = Intent(this, LaporanActivity::class.java)
        startActivity(intent)
    }

    fun onMenu3Clicked(view: View) {
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

    fun showAlert(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Peringatan")
        builder.setMessage("Ini adalah peringatan adanya bencana alam yang akan terjadi.")
        builder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }
        builder.setNegativeButton("Batal") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
