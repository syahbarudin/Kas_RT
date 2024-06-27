package id.kasrt.model



data class Message(
    var messageId: String = "",
    var senderId: String = "",
    var senderName: String = "",
    var messageText: String = "",
    var timestamp: Long = 0,
    var status: String = "sent", // Default status
    val readBy: MutableList<String> = mutableListOf()
)
