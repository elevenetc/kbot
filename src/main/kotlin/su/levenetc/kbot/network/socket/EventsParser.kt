package su.levenetc.kbot.network.socket

import com.google.gson.Gson
import io.reactivex.subjects.PublishSubject
import su.levenetc.kbot.models.Event
import su.levenetc.kbot.models.Message

class EventsParser {

    val gson = Gson()
    val publishSubject = PublishSubject.create<Event>()!!

    fun handleMessage(message: String) {
        var event = gson.fromJson(message, Event::class.java)

        if (event.type == "message") {
            event = gson.fromJson(message, Message::class.java)
        }

        publishSubject.onNext(event)
    }
}