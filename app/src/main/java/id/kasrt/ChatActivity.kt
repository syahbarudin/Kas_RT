package id.kasrt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import id.kasrt.databinding.ActivityChatBinding

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val messageText: String = "",
    val timestamp: Long = 0L
)

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

        messageAdapter = MessageAdapter(messages, auth.currentUser?.uid ?: "")
        binding.recyclerViewChat.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        binding.buttonSendMessage.setOnClickListener {
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
                    binding.recyclerViewChat.scrollToPosition(messages.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
