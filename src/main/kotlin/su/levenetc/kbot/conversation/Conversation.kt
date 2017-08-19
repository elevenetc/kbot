package su.levenetc.kbot.conversation

class Conversation(val root: Message) {

    var current: Message = root

    companion object {
        fun waitFor(message: String): Conversation {
            return Conversation(UserMessage(message))
        }

        fun startFrom(message: String): Conversation {
            return Conversation(BotMessage(message))
        }
    }

    fun expect(validator: MessageValidator): Conversation {
        val next = UserMessage(validator)
        current.next = next
        current = next
        return this
    }

    fun respondWith(message: String): Conversation {
        val next = BotMessage(message)
        current.next = next
        current = next
        return this
    }

    fun end(): Conversation {
        current.next = EndMessage()
        return this
    }


}