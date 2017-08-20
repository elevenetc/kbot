package su.levenetc.kbot.conversation

class Conversation(val root: Message) {

    var current: Message = root

    companion object {
        fun waitFor(message: String): Conversation {
            return Conversation(UserMessage(message))
        }

        fun initWith(message: String): Conversation {
            return Conversation(BotMessage(message))
        }
    }

//    fun getResponseAndSend(message: String): Conversation {
//        val next = UserMessage()
//        current.next.add(next)
//        next.next.add(BotMessage(message))
//        current = next.next
//        return this
//    }
//
//    fun getResponseAndEnd(): Conversation {
//        val next = UserMessage()
//        current.next = next
//        next.next = EndMessage()
//        current = next.next
//        return this
//    }
//
//
//    fun expect(validator: MessageValidator): Conversation {
//        val next = UserMessage(validator)
//        current.next.add(next)
//        current = next
//        return this
//    }
//
//    fun respondWith(message: String): Conversation {
//        val next = BotMessage(message)
//        current.next.add(next)
//        current = next
//        return this
//    }
//
//    fun end(): Conversation {
//        current.next.add(EndMessage())
//        return this
//    }


}