package id.kasrt;
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging

import id.kasrt.databinding.ActivityMainBinding
import id.kasrt.model.ResponseUser
import id.kasrt.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var rv_users: RecyclerView
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rv_users = findViewById(R.id.rv_users)
        adapter = UserAdapter(mutableListOf())

        rv_users.layoutManager = LinearLayoutManager(this)
        rv_users.adapter = adapter

        getUser()

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView,
                    "Permission Notifikasi Sudah Aktif",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView,
                    "Please grant Notification permission from App Settings",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        requestPermission()
        createNotificationChannel()
        createToken()
    }

    private fun getUser() {
        val apiService = ApiConfig.getApiService()
        val client = apiService.getUsers()

        client.enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                if (response.isSuccessful) {
                    val dataArray = response.body()?.data
                    if (dataArray != null) {
                        adapter.setUsers(dataArray)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to retrieve data", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun createToken() {
        val TAG = "FCM__TOKEN"
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d(TAG, token ?: "Token is null")
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Important Notification Channel",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "This notification contains important announcement, etc."
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "ddyyfe"
    }
}
