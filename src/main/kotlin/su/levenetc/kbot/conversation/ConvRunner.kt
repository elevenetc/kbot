package su.levenetc.kbot.conversation

class ConvRunner(conversation: Conversation,
                 private val outBotMessagesHandler: OutBotMessagesHandler) {

    var current: Message = conversation.root
    var isFinished: Boolean = false

    fun start() {
        if (current is BotMessage) {
            outBotMessagesHandler.send(current.message)
            moveToNext()
        }
    }

    fun onUserMessage(message: String) {
        if (isFinished) return
        if (current is UserMessage) {
            validateAndNext(message)
        }
    }

    private fun validateAndNext(message: String) {
        val currentMessage = current as UserMessage
        if (currentMessage.validator.isValid(message)) {
            current = current.next
            checkIfFinished()
            moveToNext()
        } else {
            val errorMessage = currentMessage.validator.onError(message)
            outBotMessagesHandler.send(errorMessage)
        }
    }

    private fun checkIfFinished() {
        if (current is EndMessage) {
            isFinished = true
        }
    }

    private fun moveToNext() {
        if (!isFinished) {
            if (current is BotMessage) {
                current = current.next
                checkIfFinished()
            }
        }
    }
}