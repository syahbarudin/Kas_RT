package id.kasrt.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import id.kasrt.NotificationActivity
import id.kasrt.R
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "ddyyfe"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            startNotification(message.notification?.title, message.notification?.body)
        }
    }

    private fun startNotification(title: String?, message: String?) {
        val intent = Intent(this, NotificationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.bg_card_laporan)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random().nextInt(), builder.build())
        // Menyimpan notifikasi ke penyimpanan lokal
        saveNotificationToLocal(title, message)
    }

    private fun saveNotificationToLocal(title: String?, message: String?) {
        val sharedPreferences =
            applicationContext.getSharedPreferences("NotificationHistory", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val timestamp = System.currentTimeMillis()
        editor.putString(timestamp.toString(), "$title|$message|$timestamp")
        editor.apply()
    }
}
