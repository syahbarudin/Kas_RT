package id.kasrt

import android.os.Bundle
import android.widget.Toast
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
    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("messages")

        messageAdapter = MessageAdapter(messages, auth.currentUser?.uid ?: "") { message ->
            showDeleteMessageDialog(message)
        }
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.editTextMessage.text.clear()
            }
        }

        listenForMessages()
    }

    private fun sendMessage(messageText: String) {
        val userId = auth.currentUser?.uid ?: return
        val userEmail = auth.currentUser?.email ?: "Unknown User"

        val message = Message(
            messageId = database.push().key ?: "",
            senderId = userId,
            senderName = userEmail,
            messageText = messageText,
            timestamp = System.currentTimeMillis()
        )
        database.child(message.messageId).setValue(message)
    }

    private fun listenForMessages() {
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messages.add(message)
                    messageAdapter.notifyItemInserted(messages.size - 1)
                    binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    val index = messages.indexOfFirst { it.messageId == message.messageId }
                    if (index != -1) {
                        messages.removeAt(index)
                        messageAdapter.notifyItemRemoved(index)
                    }
                }
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showDeleteMessageDialog(message: Message) {
        // Tampilkan dialog konfirmasi hapus pesan
        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        dialogBuilder.setMessage("Apakah Anda yakin ingin menghapus pesan ini?")
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                deleteMessage(message)
            }
            .setNegativeButton("Tidak") { dialog, id ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Hapus Pesan")
        alert.show()
    }

    private fun deleteMessage(message: Message) {
        database.child(message.messageId).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Pesan dihapus", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal menghapus pesan", Toast.LENGTH_SHORT).show()
        }
    }
}
