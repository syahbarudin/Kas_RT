package id.kasrt

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.kasrt.model.NotificationItem

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        displayNotificationHistory()
    }

    private fun displayNotificationHistory() {
        val sharedPreferences =
            applicationContext.getSharedPreferences("NotificationHistory", Context.MODE_PRIVATE)
        val notifications = mutableListOf<NotificationItem>()
        val entries = sharedPreferences.all
        for ((_, value) in entries) {
            val notificationData = value.toString().split("|")
            if (notificationData.size == 3) {
                val title = notificationData[0]
                val content = notificationData[1]
                val timestamp = notificationData[2].toLong()
                notifications.add(NotificationItem(title, content, timestamp))
            }
        }
        notifications.sortByDescending { it.timestamp }
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NotificationHistoryAdapter(notifications)
    }
}
