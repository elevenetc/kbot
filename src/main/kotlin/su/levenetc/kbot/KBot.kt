package su.levenetc.kbot

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import su.levenetc.kbot.models.RtmState
import su.levenetc.kbot.network.buildHttpApi
import su.levenetc.kbot.network.socket.WebSocketClient
import su.levenetc.kbot.utils.fatalStop


class KBot(val token: String) {

    var httpApi = buildHttpApi()

    init {
        if (token.isNullOrEmpty()) fatalStop("No defined Slack token")
    }

    fun start() {
        val rtmState = httpApi.rtmConnect(token)
        rtmState.enqueue(object : Callback<RtmState> {
            override fun onFailure(call: Call<RtmState>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<RtmState>?, response: Response<RtmState>) {
                val rtmState = response.body()

                if (rtmState != null) {
                    println(rtmState.url)
                    WebSocketClient("wss://echo.websocket.org").connect()
                    //WebSocketClient(rtmState.url).connect()
                }


            }

        })
    }

}