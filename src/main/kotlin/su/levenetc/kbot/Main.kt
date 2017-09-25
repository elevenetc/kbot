package su.levenetc.kbot

import com.google.gson.Gson
import io.reactivex.Observable
import su.levenetc.kbot.conversation.*
import su.levenetc.kbot.models.Event
import su.levenetc.kbot.models.Message


class Main

fun main(args: Array<String>) {
    val kBot = KBot(System.getenv("SLACK_TOKEN"))
    //startPingPong(kBot)
//    startSurvey(kBot)
    loadDataById(kBot)
}

private fun loadDataById(kBot: KBot) {
    val model = waitForUserMessage("load")
            .then(BotMessage("which id?")
                    .then(actOnUserMessage(object : MessageAction {
                        override fun act(message: String) {
                            Thread.sleep(5000)
                        }

                    })
                            .then(BotMessage("handled").andFinish())
                    )
            )
            .build()

    kBot.start(KBot.ConversationCallback(model))
}

private fun startSurvey(kBot: KBot) {
    val model = initFromBotMessage("Hello, are you hungry?")
            .then(
                    anyUserMessage()
                            .andFinish("Thanks, bye!")
            )
            .build()
    kBot.start(object : KBot.InitCallback {


        var conversation: Conversation? = null

        override fun onError() {

        }

        override fun onSuccess(eventsObservable: Observable<Event>, writer: KBot.Writer) {

            //userId: U15AQ3222
            //channel: D1B12H674

            if (conversation == null) {
                conversation = Conversation(model, SlackOutMessageHandler("D1B12H674", writer)).start()
            }
            1
            eventsObservable.filter({
                it is Message
            }).subscribe {
                val message = it as Message
                conversation?.onUserMessage(message.text)
            }
        }

    })
}

private fun startPingPong(kBot: KBot) {
    kBot.start(
            KBot.ConversationCallback(
                    waitForUserMessage("ping").andFinish("pong").build()
            )
    )
}

class SlackOutMessageHandler(
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