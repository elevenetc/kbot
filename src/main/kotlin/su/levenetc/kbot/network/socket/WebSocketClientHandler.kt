package su.levenetc.kbot.network.socket

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.websocketx.*

class WebSocketClientHandler(val handShaker: WebSocketClientHandshaker) : SimpleChannelInboundHandler<Any>() {

    lateinit var channelPromise: ChannelPromise

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        println("handleAdded")
        channelPromise = ctx.newPromise()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        println("channelActive")
        handShaker.handshake(ctx.channel())
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("channelInactive")
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        println("userEventTriggered")
        super.userEventTriggered(ctx, evt)
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        println("handlerRemoved")
        super.handlerRemoved(ctx)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        println("channelRead0: $msg")
        val ch = ctx.channel()
        if (!handShaker.isHandshakeComplete) {
            handShaker.finishHandshake(ch, msg as FullHttpResponse)
            channelPromise.setSuccess()
            return
        }

        val frame = msg as WebSocketFrame
        if (frame is TextWebSocketFrame) {
            val text = frame.text()
            println("text message: $text")
        } else if (frame is PongWebSocketFrame) {
            println("pont message")
        } else if (frame is CloseWebSocketFrame) {
            ch.close()
        } else {
            println("unhandled frame: $frame")
        }
    }

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        println("channelRead: $msg")
        super.channelRead(ctx, msg)
    }

    override fun acceptInboundMessage(msg: Any?): Boolean {
        println("acceptInboundMessage: $msg")
        return super.acceptInboundMessage(msg)
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        println("channelUnregistered")
        super.channelUnregistered(ctx)
    }

    override fun channelRegistered(ctx: ChannelHandlerContext?) {
        println("channelRegistered")
        super.channelRegistered(ctx)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        println("channelReadComplete")
        super.channelReadComplete(ctx)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        println("exceptionCaught")
        cause.printStackTrace()
        if (!channelPromise.isDone) channelPromise.setFailure(cause)
        ctx.close()
    }
}