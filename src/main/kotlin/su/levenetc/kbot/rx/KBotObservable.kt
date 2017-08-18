package su.levenetc.kbot.rx

import io.reactivex.Observable
import su.levenetc.kbot.models.Event

class KBotObservable(val obs: Observable<Event>) {

//    fun allMessages(): Observable<Event> {
//        return obs
//    }
//
//    fun textMessages(): Observable<Message> {
//        return obs.flatMap {  }
//    }
//
//    fun directMessages(): Observable<Message> {
//        return obs
//    }
//
//    fun mentionMessages(): Observable<Message> {
//        return obs
//    }

}