package su.levenetc.kbot.services


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import su.levenetc.kbot.utils.getField


/**
 * Created by eugene.levenetc on 13/07/2017.
 */
class SocketHandler : WebSocketHandler {

    var logger: Logger = LoggerFactory.getLogger(SocketHandler::class.java)
    var session: WebSocketSession? = null

    override fun handleTransportError(session: WebSocketSession?, exception: Throwable?) {
        logger.info("handleTransportError")
    }

    override fun afterConnectionClosed(session: WebSocketSession?, closeStatus: CloseStatus?) {
        logger.info("afterConnectionClosed")
    }

    override fun handleMessage(session: WebSocketSession?, message: WebSocketMessage<*>?) {

        val payload: String = message?.payload.toString()
        logger.info(payload)
        val type = getField(payload)
        logger.info(type)

        if (type == "presence_change") {

        } else if (type == "reconnect_url") {

        } else if (type == "hello") {

        }

        //session?.sendMessage(TextMessage(""))
        //logger.info("handleMessage: {}", payload)
    }

    override fun afterConnectionEstablished(session: WebSocketSession?) {
        logger.info("afterConnectionEstablished")
        this.session = session!!
    }

    override fun supportsPartialMessages(): Boolean {
        logger.info("supportsPartialMessages")
        return false
    }

    fun stop() {
        session?.close()
    }
}