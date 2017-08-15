package su.levenetc.kbot.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun buildHttpApi(): HttpApi {
    val retrofit = Retrofit.Builder()
            .baseUrl("https://slack.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    return retrofit.create(HttpApi::class.java)
}