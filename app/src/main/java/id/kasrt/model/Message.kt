package id.kasrt.model

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val messageText: String = "",
    val timestamp: Long = 0L
)
