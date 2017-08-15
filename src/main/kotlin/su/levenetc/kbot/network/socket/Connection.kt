package su.levenetc.kbot.network.socket

import network.ConnectionHandler
import network.ISocket
import network.MessageInterceptor
import su.levenetc.kbot.utils.interruptSilently
import java.util.concurrent.LinkedBlockingDeque

class Connection(val socket: ISocket, val connection: ConnectionHandler) {

    @Volatile
    var interrupted: Boolean = false

    @Volatile
    var connected: Boolean = true

    val queue: LinkedBlockingDeque<String> = LinkedBlockingDeque()

    init {
        connection.messageInterceptor = object : MessageInterceptor {
            override fun onIntercepted(message: String) {
                queue.add(message)
            }
        }
    }

    private val outThread = Thread({
        output()
    })

    private val inThread = Thread({
        input()
    })

    fun connect() {
        outThread.start()
        inThread.start()
        connection.onReady()
    }

    fun output() {
        try {
            while (connected) {
                val data = queue.take()
                socket.write(data)
            }
        } catch (e: Exception) {
            interruptOnError(e)
        }
    }

    fun input() {
        try {
            while (connected) {
                connection.onMessage(socket.read())
            }
        } catch (e: Exception) {
            interruptOnError(e)
        }
    }

    @Synchronized
    private fun interruptOnError(e: Exception) {
        if (!interrupted) {
            connection.onError(e)
            interrupt()
        }
    }

    fun interrupt() {
        if (!interrupted) {
            interrupted = true
            stopThreads()
        }
    }

    fun stopThreads() {
        interruptSilently(outThread)
        interruptSilently(inThread)
    }

}