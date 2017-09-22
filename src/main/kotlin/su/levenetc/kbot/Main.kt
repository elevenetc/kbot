package su.levenetc.kbot

import com.google.gson.Gson
import io.reactivex.Observable
import su.levenetc.kbot.conversation.Conversation
import su.levenetc.kbot.conversation.OutMessagesHandler
import su.levenetc.kbot.conversation.waitForUserMessage
import su.levenetc.kbot.models.Event
import su.levenetc.kbot.models.Message


class Main

fun main(args: Array<String>) {
    val kBot = KBot(System.getenv("SLACK_TOKEN"))

    kBot.start(object : KBot.InitCallback {

        override fun onError() {

        }

        override fun onSuccess(eventsObservable: Observable<Event>, writer: KBot.Writer) {
            //,
            //kBotObservable: KBotObservable,
            //users: List<User>) {

            val model = waitForUserMessage("ping").andFinish("pong")
            var conversation: Conversation? = null

            eventsObservable.filter({
                it is Message
            }).subscribe {

                val message = it as Message

                if (conversation == null) {
                    conversation = Conversation(model, SlackOutMessageHandler(message.user, message.channel, writer))
                }

                conversation?.onUserMessage(message.text)
            }


//            con.send("message")
//                    .cancelDelay(2000)
//                    //.validateResponse(ResponseValidator())
//                    .nextOnAnyResponse("optional message back", "optional message handler")
//                    .nextOnlyOn("specific message or validator", "fixing message")
//                    .thenFinish("optional message")
//
//            con.start("user-id or empty")
//
//            //TODO: add tree conversation
//
//            val pingPong = Conversation()
//            pingPong.onMessage("ping", "optional filer: direct, mentioned")
//                    .send("pong")
        }

    })

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