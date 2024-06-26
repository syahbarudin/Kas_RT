package id.kasrt.model

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val messageText: String = "",
    val timestamp: Long = 0L,
    val status: String = ""
) {
    companion object {
        const val STATUS_SENT = 0
        const val STATUS_DELIVERED = 1
        const val STATUS_READ = 2
    }
}
