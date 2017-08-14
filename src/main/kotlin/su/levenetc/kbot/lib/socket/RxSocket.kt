package su.levenetc.kbot.lib.socket

import io.reactivex.Single

interface RxSocket {
    fun connect(): Single<RxSocket>
    fun disconnect(): Single<RxSocket>
}