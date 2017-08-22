package su.levenetc.kbot.conversation

class BotMessage : Message {

    var userMessage: String = ""
    private val messageHandler: MessageHandler

    constructor(msg: String) : super(msg) {
        messageHandler = StaticMessageHandler(msg)
    }

    constructor(messageHandler: MessageHandler) : super("") {
        this.messageHandler = messageHandler
    }

    override var message: String
        get() {
            return messageHandler.get(super.message)
        }
        set(value) {
            super.message = value
        }
}

interface MessageHandler {
    fun get(userMessage: String): String
}

class StaticMessageHandler(val message: String) : MessageHandler {
    override fun get(userMessage: String): String {
        return message
    }
}

fun initFromBotMessage(message: String): BotMessage {
    return BotMessage(message)
}