package su.levenetc.kbot.network.socket

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketVersion
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import java.net.URI
import java.util.*


class WebSocketClient(val uri: String) {

    lateinit var ch: Channel

    fun connect() {
        val bootstrap = Bootstrap()
        val uri: URI = URI.create(uri)
        val handler = WebSocketClientHandler(WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, false, HttpHeaders.EMPTY_HEADERS, 1280000))

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

    inner class TimeClientHandler : ChannelInboundHandlerAdapter() {
        override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
            val m = msg as ByteBuf // (1)
            try {
                val currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L
                System.out.println(Date(currentTimeMillis))
                ctx.close()
            } finally {
                m.release()
            }
        }

        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
            cause.printStackTrace()
            ctx.close()
        }
    }
}