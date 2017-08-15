package network

import java.net.ServerSocket
import java.net.Socket

class JavaSocketFactory(val host: String, val port: Int) : SocketFactory {

    override fun serverSocket(): IServerSocket = object : IServerSocket {

        val serverSocket = ServerSocket(port)

        override fun accept(): ISocket {
            return JavaSocket(serverSocket.accept())
        }
    }

    override fun clientSocket(): ISocket {
        return JavaSocket(Socket(host, port))
    }

}