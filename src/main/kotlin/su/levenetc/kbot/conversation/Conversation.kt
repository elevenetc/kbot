package su.levenetc.kbot.conversation

class Conversation(conversationModel: ConversationModel,
                   private val outMessagesHandler: OutMessagesHandler) {

    private var current: Message = conversationModel.rootMessage
    private var multipleUserVariants: Boolean = false
    var isFinished: Boolean = false

    fun start(): Conversation {
        if (current is BotMessage)
            triggerBotMessage()
        return this
    }

    fun onUserMessage(msg: String): Conversation {
        if (isFinished) return this

        if (multipleUserVariants) {

            for (message in current.next) {
                if (message.validator.isValid(msg)) {
                    current = message
                    multipleUserVariants = false
                    handleUserMessageAndMoveToNext(msg)
                    return this
                }
            }

            println("not handled message: $msg!")

        } else {
            if (current is UserMessage) {
                handleUserMessageAndMoveToNext(msg)
            } else {
                throw RuntimeException("invalid state. bot msg is expected: " + current)
            }
        }

        return this
    }

    private fun handleUserMessageAndMoveToNext(message: String) {
        val currentMessage = current as UserMessage
        if (currentMessage.validator.isValid(message)) {

            val nextMessage = current.next[0]
            moveToNext(nextMessage)

        } else {
            val errorMessage = currentMessage.validator.onError(message)
            if (errorMessage.isNotEmpty())
                outMessagesHandler.send(errorMessage)
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
            outMessagesHandler.send(endMessage.message)
        }
    }

    private fun triggerBotMessage() {
        outMessagesHandler.send(current.message)
        if (current.next.size == 1) {
            multipleUserVariants = false
            moveToNext(current.next[0])
        } else {
            multipleUserVariants = true
        }
    }

}