package su.levenetc.kbot.lib

import okhttp3.OkHttpClient
import okhttp3.Request


class KBot {

    var client = OkHttpClient()

    lateinit var token: String

    fun connect(token: String) {
        val request = Request.Builder()
                .url("https://slack.com/api/rtm.connect?token=$token")
                .build()

        val response = client.newCall(request).execute()
        val string = response.body()!!.string()

    }

}