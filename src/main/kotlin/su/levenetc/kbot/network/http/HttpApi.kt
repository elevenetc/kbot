package su.levenetc.kbot.network.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import su.levenetc.kbot.models.RtmState

interface HttpApi {
    @GET("api/rtm.start")
    fun rtmConnect(@Query("token") token: String): Call<RtmState>
}