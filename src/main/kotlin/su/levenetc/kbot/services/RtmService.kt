package su.levenetc.kbot.services

import su.levenetc.kbot.models.User


/**
 * Created by eugene.levenetc on 13/07/2017.
 */
class RtmService(val token: String) {

    //val socketHandler = SocketHandler()

    init {



        //val rtmState: RtmState = restTemplate.getForObject("https://slack.com/api/rtm.start?token=$token", RtmState::class.java)
        //socketConnection = WebSocketConnectionManager(StandardWebSocketClient(), socketHandler, rtmState.url)
        //socketConnection.start()
    }

    fun someRestCall(name: String): User {
        return User()
        //return restTemplate.getForObject("/users", User::class.java, name)
        //return restTemplate.getForObject("/{name}/details", User::class.java, name)
    }

    fun preDestroy() {
        //socketHandler.stop()
    }

}