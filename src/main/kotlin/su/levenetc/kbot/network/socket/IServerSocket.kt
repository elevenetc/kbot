package network

interface IServerSocket {
    fun accept(): ISocket
}