package id.kasrt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import id.kasrt.model.Message


class MessageAdapter(
    private val messages: List<Message>,
    private val onLongClick: (Message) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSenderName: TextView = itemView.findViewById(R.id.tvSenderName)
        val tvMessageText: TextView = itemView.findViewById(R.id.tvMessageText)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
    }

    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSenderName: TextView = itemView.findViewById(R.id.tvSenderName)
        val tvMessageText: TextView = itemView.findViewById(R.id.tvMessageText)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUserId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        val senderName = message.senderName.substringBefore("@")

        if (holder is SentMessageViewHolder) {
            holder.tvSenderName.text = senderName
            holder.tvMessageText.text = message.messageText
            holder.tvTimestamp.text = android.text.format.DateFormat.format("hh:mm a", message.timestamp)
            holder.itemView.setOnLongClickListener {
                onLongClick(message)
                true
            }
        } else if (holder is ReceivedMessageViewHolder) {
            holder.tvSenderName.text = senderName
            holder.tvMessageText.text = message.messageText
            holder.tvTimestamp.text = android.text.format.DateFormat.format("hh:mm a", message.timestamp)
            holder.itemView.setOnLongClickListener {
                onLongClick(message)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
