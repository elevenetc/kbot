package su.levenetc.kbot.conversation

class OneSelection : Condition {
    override fun getIndex(message: String): Int {
        return 0
    }
}