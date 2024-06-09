package id.kasrt

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.kasrt.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 12000
    private lateinit var binding: ActivitySplashBinding
    private var counter = 0
    private var isFirstTime = true // Flag to track if it's the first time opening the app
    private var isConnected = false // Flag to track internet connection status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        prog()
        checkFirebaseConnection()
    }

    private fun prog() {
        val pb: ProgressBar = binding.pb
        val percentageTextView = binding.progressPercentageTextView
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                counter++
                pb.progress = counter
                val percentage = "${counter}%"
                percentageTextView.text = percentage

                // Atur posisi TextView di atas ProgressBar
                val layoutParams = percentageTextView.layoutParams as RelativeLayout.LayoutParams
                layoutParams.bottomMargin = 20 // Jarak antara ProgressBar dan TextView
                percentageTextView.layoutParams = layoutParams

                if (counter < 100) {
                    handler.postDelayed(this, calculateTimeout() / 100)
                }
            }
        }
        handler.postDelayed(runnable, 0)
    }



    private fun calculateTimeout(): Long {
        // Calculate the timeout based on the network connection status
        return if (isConnected) {
            SPLASH_TIME_OUT / 2 // If connected, reduce the timeout
        } else {
            SPLASH_TIME_OUT // Otherwise, use the default timeout
        }
    }

    private fun checkFirebaseConnection() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val connected = networkInfo != null && networkInfo.isConnected

        if (isFirstTime) {
            if (!connected) {
                showErrorAndClose()
            } else {
                isConnected = true
                Handler(Looper.getMainLooper()).postDelayed({
                    startNextActivity()
                }, calculateTimeout())
            }
            isFirstTime = false
        } else {
            if (!connected) {
                isConnected = false
                startNextActivity()
            } else {
                isConnected = true
                startNextActivity()
            }
        }
    }

    private fun startNextActivity() {
        if (isConnected) {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun showErrorAndClose() {
        Toast.makeText(this@SplashActivity, "Tidak ada koneksi. Mohon periksa jaringan anda dan silahkan coba kembali.", Toast.LENGTH_LONG).show()
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 10000)
    }
}
