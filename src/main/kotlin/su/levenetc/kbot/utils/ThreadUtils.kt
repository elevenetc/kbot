package su.levenetc.kbot.utils

fun interruptSilently(t: Thread) {
    try {
        t.interrupt()
    } catch (e: Throwable) {
        //ignore
    }
}