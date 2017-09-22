package su.levenetc.kbot.conversation

class Conversation(root: Message,
                   private val outBotMessagesHandler: OutBotMessagesHandler) {

    var current: Message = root
    var isFinished: Boolean = false
    var multipleUserVariants: Boolean = false

    fun start() {
        if (current is BotMessage)
            triggerBotMessage()
    }

    fun onUserMessage(msg: String) {
        if (isFinished) return

        if (multipleUserVariants) {

            for (message in current.next) {
                if (message.validator.isValid(msg)) {
                    current = message
                    multipleUserVariants = false
                    handleUserMessageAndMoveToNext(msg)
                    return
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


    }

    private fun handleUserMessageAndMoveToNext(message: String) {
        val currentMessage = current as UserMessage
        if (currentMessage.validator.isValid(message)) {

            //val nextIndex = current.condition.getIndex(message)
            val nextMessage = current.next[0]

            moveToNext(nextMessage)

//            if (nextMessage is BotMessage) {
//                moveToNext(nextMessage)
//            } else if (nextMessage is EndMessage) {
//
//            }

            //(nextMessage as BotMessage).userMessage = message
            //handle user message(store?)

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
        if (current.next.size == 1) {
            multipleUserVariants = false
            moveToNext(current.next[0])
        } else {
            multipleUserVariants = true
        }
    }

}