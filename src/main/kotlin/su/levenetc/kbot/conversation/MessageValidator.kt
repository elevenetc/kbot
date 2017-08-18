package su.levenetc.kbot.conversation

interface MessageValidator {
    fun isValid(message: String): Boolean
    fun onError(message: String): String
}