package su.levenetc.kbot.conversation

class BotMessage(message: String) : Message(message) {

}

fun initFromBotMessage(message: String): BotMessage {
    return BotMessage(message)
}