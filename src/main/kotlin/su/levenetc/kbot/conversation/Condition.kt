package su.levenetc.kbot.conversation

interface Condition {
    fun getIndex(message: String): Int
}