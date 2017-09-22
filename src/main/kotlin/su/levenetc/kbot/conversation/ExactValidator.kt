package su.levenetc.kbot.conversation

class ExactValidator(
        private val target: String,
        private val errorMessage: String = "") : MessageValidator {

    override fun isValid(message: String): Boolean {
        return target == message
    }

    override fun onError(message: String): String {
        return errorMessage
    }

}