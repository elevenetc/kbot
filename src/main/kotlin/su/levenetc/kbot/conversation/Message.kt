package su.levenetc.kbot.conversation

open class Message(
        var message: String = "",
        var validator: MessageValidator = EmptyMessageValidator()
) {
    lateinit var next: Message
}