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
        adapter = MessageAdapter(messages) { message -> showDeleteAlert(message) }

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
        updateMessageStatusToRead()
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
                    message?.let {
                        if (it.senderId != auth.currentUser?.uid && it.status == "sent") {
                            it.status = "received"
                            database.child(it.messageId).child("status").setValue("received")
                        }
                        messages.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
                binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Gagal memuat pesan", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDeleteAlert(message: Message) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Pesan")
            .setMessage("Apakah Anda yakin ingin menghapus pesan ini?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteMessage(message)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteMessage(message: Message) {
        if (message.senderId == auth.currentUser?.uid) {
            database.child(message.messageId).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@ChatActivity, "Pesan berhasil dihapus.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ChatActivity, "Gagal menghapus pesan", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this@ChatActivity, "Anda tidak dapat menghapus pesan ini", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMessageStatusToRead() {
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null && message.senderId != auth.currentUser?.uid && message.status == "received") {
                    snapshot.ref.child("status").setValue("read")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
