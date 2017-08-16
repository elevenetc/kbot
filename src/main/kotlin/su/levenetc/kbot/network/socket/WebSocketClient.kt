package su.levenetc.kbot.network.socket

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.EmptyHttpHeaders
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketVersion
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.reactivex.Observable
import su.levenetc.kbot.models.Event
import java.net.URI


class WebSocketClient(val uri: String) {

    private lateinit var ch: Channel
    private val eventsParser = EventsParser()

    fun connect() {
        val bootstrap = Bootstrap()
        val uri: URI = URI.create(uri)
        val handler = WebSocketClientHandler(
                WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, false, EmptyHttpHeaders.INSTANCE, 1280000),
                eventsParser
        )

        bootstrap.group(NioEventLoopGroup())
                .channel(NioSocketChannel::class.java)
                .handler(object : ChannelInitializer<SocketChannel>() {

                    val sslCtx: SslContext = SslContextBuilder.forClient().build()

                    override fun initChannel(ch: SocketChannel) {
                        val pipeline = ch.pipeline()
                        pipeline.addLast("ssl-handler", sslCtx.newHandler(ch.alloc(), uri.host, 443))
                        pipeline.addLast("http-codec", HttpClientCodec())
                        pipeline.addLast("aggregator", HttpObjectAggregator(65536))
                        pipeline.addLast("ws-handler", handler)
                    }
                })
        ch = bootstrap.connect(uri.host, 443).sync().channel()
        handler.channelPromise.sync()
    }

    fun close() {
        ch.writeAndFlush(CloseWebSocketFrame())
        ch.closeFuture().sync()
    }

    fun eval(text: String) {
        ch.writeAndFlush(TextWebSocketFrame(text))
    }

    fun eventsObservable(): Observable<Event> {
        return eventsParser.publishSubject
    }
}