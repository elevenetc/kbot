package su.levenetc.kbot

import com.google.gson.Gson
import su.levenetc.kbot.conversation.OutMessagesHandler
import su.levenetc.kbot.conversation.waitForUserMessage


class Main

fun main(args: Array<String>) {
    val kBot = KBot(System.getenv("SLACK_TOKEN"))
    startPingPong(kBot)
}

private fun startPingPong(kBot: KBot) {
    kBot.start(
            KBot.ConversationCallback(
                    waitForUserMessage("ping").andFinish("pong").build()
            )
    )
}

class SlackOutMessageHandler(
        val userId: String,
        val channgelId: String,
        val writer: KBot.Writer
) : OutMessagesHandler {

    val gson: Gson = Gson()
    var messageId: Int = -1

    override fun send(message: String) {
        messageId++
        writer.write(gson.toJson(SlackOutMessage(messageId, message, channgelId)))
    }
}

class SlackOutMessage(val id: Int, val text: String, val channel: String) {
    val type: String = "message"
}

class Node(val value: Int) {
    var left: Node? = null
    var right: Node? = null
}