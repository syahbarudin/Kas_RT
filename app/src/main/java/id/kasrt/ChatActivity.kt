package id.kasrt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.kasrt.databinding.ActivityChatBinding
import id.kasrt.model.Message

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var adapter: MessageAdapter
    private lateinit var messages: MutableList<Message>
    private var currentUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("messages")
        messages = mutableListOf()
        adapter = MessageAdapter(messages) { message -> deleteMessage(message) }

        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMessages.adapter = adapter

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.editTextMessage.text.clear()
            }
        }

        // Mengambil nama user dari email
        currentUser = auth.currentUser?.email?.substringBefore("@")

        loadMessages()
    }

    private fun sendMessage(messageText: String) {
        val message = Message(
            senderId = auth.currentUser?.uid ?: "",
            senderName = auth.currentUser?.email ?: "Unknown User",
            messageText = messageText,
            timestamp = System.currentTimeMillis()
        )
        database.push().setValue(message)
    }

    private fun loadMessages() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    message?.let { messages.add(it) }
                }
                adapter.notifyDataSetChanged()
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Gagal memuat pesan", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDeleteConfirmationDialog(message: Message) {
        AlertDialog.Builder(this)
            .setMessage("Apakah Anda yakin ingin menghapus pesan ini?")
            .setPositiveButton("Ya") { _, _ -> deleteMessage(message) }
            .setNegativeButton("Tidak", null)
            .show()
    }
    private fun deleteMessage(message: Message) {
        // Hapus pesan dari Firebase database
        database.child(message.messageId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@ChatActivity,
                    "Pesan berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@ChatActivity,
                    "Gagal menghapus pesan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
