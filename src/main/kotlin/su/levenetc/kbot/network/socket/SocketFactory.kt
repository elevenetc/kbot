package network

interface SocketFactory {
    fun clientSocket(): ISocket
    fun serverSocket(): IServerSocket
}