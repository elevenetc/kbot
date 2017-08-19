package su.levenetc.kbot.conversation

class ConversationRunner(conversation: Conversation,
                         private val outBotMessagesHandler: OutBotMessagesHandler) {

    var current: Message = conversation.root
    var isFinished: Boolean = false

    fun start() {
        if (current is BotMessage)
            triggerBotMessage()
    }

    fun onUserMessage(message: String) {
        if (isFinished) return
        if (current is UserMessage) {
            handleUserMessageAndMoveToNext(message)
        } else {
            throw RuntimeException("invalid state. bot message is expected: " + current)
        }
    }

    private fun handleUserMessageAndMoveToNext(message: String) {
        val currentMessage = current as UserMessage
        if (currentMessage.validator.isValid(message)) {

            //handle user message(store?)

            moveToNext()
        } else {
            val errorMessage = currentMessage.validator.onError(message)
            if (errorMessage.isNotEmpty())
                outBotMessagesHandler.send(errorMessage)
        }
    }

    private fun moveToNext() {
        if (!isFinished) {
            current = current.next
            if (current is EndMessage) isFinished = true
            else if (current is BotMessage) triggerBotMessage()
        }
    }

    private fun triggerBotMessage() {
        outBotMessagesHandler.send(current.message)
        moveToNext()
    }

}