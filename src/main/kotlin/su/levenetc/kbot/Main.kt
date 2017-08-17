package su.levenetc.kbot

import io.reactivex.Observable
import su.levenetc.kbot.models.Event
import su.levenetc.kbot.models.Message
import su.levenetc.kbot.rx.ConversationBuilder


class Main

fun main(args: Array<String>) {
    val kBot = KBot(System.getenv("SLACK_TOKEN"))

    kBot.start(object : KBot.InitCallback {

        override fun onError() {

        }

        override fun onSuccess(eventsObservable: Observable<Event>) {
            eventsObservable.filter({
                it is Message
            }).subscribe {
                println((it as Message).text)
            }

            val con = ConversationBuilder("user")
            con.send("message")
                    .cancelDelay(2000)
                    .validateResponse(ResponseValidator())
                    .nextOnAnyResponse("optional message back", "optional message handler")
                    .nextOnlyOn("specific message or validator", "fixing message")
                    .thenFinish("optional message")
        }

    })


}

interface OnResponse {
    fun handle(resp: String): String
}

interface ResponseValidator {

}