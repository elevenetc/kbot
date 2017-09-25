package su.levenetc.kbot.conversation

interface MessageAction {
    fun act(message: String)
}