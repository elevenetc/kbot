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
            eventsObservable.filter({
                it is Message
            }).subscribe {
                println((it as Message).text)
            }
        }

    })

}