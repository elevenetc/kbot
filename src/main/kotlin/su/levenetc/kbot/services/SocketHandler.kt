package su.levenetc.kbot.services


import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

/**
 * Created by eugene.levenetc on 13/07/2017.
 */
class SocketHandler : WebSocketHandler {

    var logger: Logger = LogManager.getFormatterLogger(SocketHandler::class.java)
    var session: WebSocketSession? = null

    override fun handleTransportError(session: WebSocketSession?, exception: Throwable?) {
        logger.info("handleTransportError")
    }

    override fun afterConnectionClosed(session: WebSocketSession?, closeStatus: CloseStatus?) {
        logger.info("afterConnectionClosed")
    }

    override fun handleMessage(session: WebSocketSession?, message: WebSocketMessage<*>?) {
        //logger.log()
        logger.debug("handleMessage:%s", message.toString())
        logger.info("handleMessage:%s", message.toString())
        logger.info("hello")
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