package su.levenetc.kbot

import io.reactivex.Observable
import su.levenetc.kbot.models.Event
import su.levenetc.kbot.models.Message


class Main

fun main(args: Array<String>) {
    val kBot = KBot(System.getenv("SLACK_TOKEN"))

    kBot.start(object : KBot.InitCallback {

        override fun onError() {

        }

        override fun onSuccess(eventsObservable: Observable<Event>) {
            //,
            //kBotObservable: KBotObservable,
            //users: List<User>) {
            eventsObservable.filter({
                it is Message
            }).subscribe {
                println((it as Message).text)
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

class Node(val value: Int) {
    var left: Node? = null
    var right: Node? = null
}