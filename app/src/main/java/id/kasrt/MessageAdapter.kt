package id.kasrt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.kasrt.model.Message

class MessageAdapter(
    private val messages: List<Message>,
    private val currentUserId: String,
    private val onMessageLongClick: (Message) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(view, onMessageLongClick)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.senderId == currentUserId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    class SentMessageViewHolder(itemView: View, private val onMessageLongClick: (Message) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.textViewMessage)
        private val senderNameTextView: TextView = itemView.findViewById(R.id.textViewSenderName)

        fun bind(message: Message) {
            messageTextView.text = message.messageText
            senderNameTextView.text = message.senderName
            itemView.setOnLongClickListener {
                onMessageLongClick(message)
                true
            }
        }
    }

    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.textViewMessage)
        private val senderNameTextView: TextView = itemView.findViewById(R.id.textViewSenderName)

        fun bind(message: Message) {
            messageTextView.text = message.messageText
            senderNameTextView.text = message.senderName
        }
    }
}
