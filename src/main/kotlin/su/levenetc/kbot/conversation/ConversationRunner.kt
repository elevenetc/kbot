package su.levenetc.kbot.conversation

class ConversationRunner(root: Message,
                         private val outBotMessagesHandler: OutBotMessagesHandler) {

    var current: Message = root
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

            val nextIndex = current.condition.getIndex(message)
            val next = current.next[nextIndex]

            //handle user message(store?)

            moveToNext(next)
        } else {
            val errorMessage = currentMessage.validator.onError(message)
            if (errorMessage.isNotEmpty())
                outBotMessagesHandler.send(errorMessage)
        }
    }

    private fun moveToNext(next: Message) {
        if (!isFinished) {

            current = next

            if (current is EndMessage) {
                isFinished = true
                sendEndMessageIfExists()
            } else if (current is BotMessage) {
                triggerBotMessage()
            }
        }
    }

    private fun sendEndMessageIfExists() {
        val endMessage = current as EndMessage
        if (endMessage.message.isNotEmpty()) {
            outBotMessagesHandler.send(endMessage.message)
        }
    }

    private fun triggerBotMessage() {
        outBotMessagesHandler.send(current.message)
        moveToNext(current.next[0])
    }

}