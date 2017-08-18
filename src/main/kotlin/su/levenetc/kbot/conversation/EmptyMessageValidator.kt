package su.levenetc.kbot.conversation

class EmptyMessageValidator : MessageValidator {

    override fun onError(message: String): String {
        return ""
    }

    override fun isValid(message: String): Boolean {
        return true
    }

}