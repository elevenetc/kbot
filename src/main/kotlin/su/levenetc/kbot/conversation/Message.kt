package su.levenetc.kbot.conversation

import java.util.*

open class Message(
        open var message: String = "",
        var validator: MessageValidator = EmptyMessageValidator()
) {

    //lateinit var condition: Condition
    val next: LinkedList<Message> = LinkedList()

    fun andFinish(): Message {
        next.add(EndMessage())
        //condition = OneSelection()
        return this
    }

    fun andFinish(message: String): Message {
        val endMessage = EndMessage(message)
        next.add(endMessage)
        //condition = OneSelection()
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


}