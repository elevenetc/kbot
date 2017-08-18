package su.levenetc.kbot.conversation

class EndMessage : Message() {
    init {
        next = this
    }
}