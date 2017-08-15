package network

interface ISocket {
    fun read(): String
    fun write(data: String)
}