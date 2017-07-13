package su.levenetc.kbot.services

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.socket.client.WebSocketConnectionManager
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import su.levenetc.kbot.models.RtmState
import su.levenetc.kbot.models.User
import javax.annotation.PreDestroy


/**
 * Created by eugene.levenetc on 13/07/2017.
 */
@Service
class RtmService(@Value("\${slack.token}") token: String) {

    var logger = LoggerFactory.getLogger(RtmService::class.java)
    val restTemplate: RestTemplate = RestTemplateBuilder()
            //.rootUri(urlRtm)
            .build()
    val socketHandler = SocketHandler()
    val socketConnection: WebSocketConnectionManager

    init {
        val rtmState: RtmState = restTemplate.getForObject("https://slack.com/api/rtm.start?token=$token", RtmState::class.java)


        socketConnection = WebSocketConnectionManager(StandardWebSocketClient(), socketHandler, rtmState.url)
        socketConnection.start()
    }

    fun someRestCall(name: String): User {
        return restTemplate.getForObject("/users", User::class.java, name)
        //return restTemplate.getForObject("/{name}/details", User::class.java, name)
    }

    @PreDestroy
    fun preDestroy() {
        socketHandler.stop()
    }

}