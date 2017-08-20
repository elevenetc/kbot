package su.levenetc.kbot.conversation

import java.util.*

open class Message(
        var message: String = "",
        var validator: MessageValidator = EmptyMessageValidator()
) {

    lateinit var condition: Condition
    val next: LinkedList<Message> = LinkedList()

    fun then(vararg nodes: Message, condition: Condition = OneSelection()): Message {

        this.condition = condition
        Collections.addAll<Message>(next, *nodes)
        return this
    }
}