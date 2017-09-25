package su.levenetc.kbot.conversation

import java.util.*

open class Message(
        open var message: String = "",
        var validator: MessageValidator = EmptyMessageValidator()
) {

    val next: LinkedList<Message> = LinkedList()
    var action: MessageAction = EmptyMessageAction()

    fun build(): ConversationModel {
        return ConversationModel(this)
    }

    fun andFinish(): Message {
        next.add(EndMessage())
        return this
    }

    fun andFinish(message: String): Message {
        val endMessage = EndMessage(message)
        next.add(endMessage)
        return this
    }

    fun thenOneOf(vararg nodes: Message): Message {
        Collections.addAll<Message>(next, *nodes)
        return this
    }

    fun then(node: Message): Message {
        next.add(node)
        return this
    }

//    fun thenBot(message: String): Message {
//        next.add(BotMessage(message))
//        return this
//    }

    override fun toString(): String {
        return message
    }
}