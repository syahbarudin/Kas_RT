package id.kasrt

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
        adapter = MessageAdapter(messages, ::deleteMessage)

        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMessages.adapter = adapter

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.editTextMessage.text.clear()
            }
        }

        currentUser = auth.currentUser?.email?.substringBefore("@")
        loadMessages()
    }

    private fun sendMessage(messageText: String) {
        val messageId = database.push().key ?: ""
        val message = Message(
            messageId = messageId,
            senderId = auth.currentUser?.uid ?: "",
            senderName = auth.currentUser?.email ?: "Unknown User",
            messageText = messageText,
            timestamp = System.currentTimeMillis(),
            status = "sent"
        )
        database.child(messageId).setValue(message)
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
                updateMessageStatusToRead()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateMessageStatusToRead() {
        val userId = auth.currentUser?.uid ?: return
        messages.forEach { message ->
            if (message.senderId != userId && !message.readBy.contains(userId)) {
                message.readBy.add(userId)
                message.status = "received"
                database.child(message.messageId).setValue(message)
            }
        }
    }

    private fun deleteMessage(message: Message) {
        if (message.senderId == auth.currentUser?.uid) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Yes") { dialog, which ->
                    database.child(message.messageId).removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@ChatActivity, "Pesan berhasil dihapus", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@ChatActivity, "Pesan gagal dihapus", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            Toast.makeText(this@ChatActivity, "Tidak dapat menghapus pesan ini", Toast.LENGTH_SHORT).show()
        }
    }
}
