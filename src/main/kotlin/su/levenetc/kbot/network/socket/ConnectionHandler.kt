package network

open class ConnectionHandler {

    lateinit var messageInterceptor: MessageInterceptor

    open fun onReady() {

    }

    open fun onMessage(message: String) {

    }

    open fun onError(e: Throwable) {
        e.printStackTrace()
    }

    fun sendMessage(message: String) {
        messageInterceptor.onIntercepted(message)
    }
}

interface MessageInterceptor {
    fun onIntercepted(message: String)
}