package su.levenetc.kbot.conversation

class Conversation(conversationModel: ConversationModel,
                   private val outMessagesHandler: OutMessagesHandler) {

    private var root: Message = conversationModel.rootMessage
    private var current: Message = conversationModel.rootMessage
    private var multipleUserVariants: Boolean = false
    var log = mutableListOf<LogMessage>()
    var isFinished: Boolean = false

    fun start(): Conversation {
        if (current is BotMessage) triggerBotMessage()
        return this
    }


    fun onUserMessage(message: String): Conversation {
        if (isFinished) return this

        if (multipleUserVariants) {

            for (msg in current.next) {
                if (msg.validator.isValid(message)) {
                    current = msg
                    multipleUserVariants = false
                    handleUserMessageAndMoveToNext(message)
                    return this
                }
            }

            //TODO: add logging
            println("not handled message: $message!")

        } else {
            if (current is UserMessage) {
                handleUserMessageAndMoveToNext(message)
            } else {
                throw RuntimeException("invalid state. bot message is expected: " + current)
            }
        }

        return this
    }

    /**
     * Checks message with validator
     * and moves to next in case of success.
     * Otherwise sends bot error message if defined
     */
    private fun handleUserMessageAndMoveToNext(message: String) {
        val currentMessage = current as UserMessage
        if (currentMessage.validator.isValid(message)) {

            currentMessage.action.act(message)
            val nextMessage = current.next[0]
            logUserMessage(message)
            moveToNext(nextMessage)

        } else {
            val errorMessage = currentMessage.validator.onError(message)
            if (errorMessage.isNotEmpty()) sendBotMessage(errorMessage)
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
            sendBotMessage(endMessage.message)
        }
    }

    private fun triggerBotMessage() {
        sendBotMessage(current.message)
        if (current.next.size == 1) {
            multipleUserVariants = false
            moveToNext(current.next[0])
        } else {
            multipleUserVariants = true
        }
    }

    private fun sendBotMessage(message: String) {
        logBotMessage(message)
        outMessagesHandler.send(message)
    }

    private fun logBotMessage(message: String) {
        log.add(LogMessage(message, true))
    }

    private fun logUserMessage(message: String) {
        log.add(LogMessage(message, false))
    }

}