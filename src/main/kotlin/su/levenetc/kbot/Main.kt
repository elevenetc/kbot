package su.levenetc.kbot


class Main

fun main(args: Array<String>) {
    KBot(System.getenv("SLACK_TOKEN")).start()
}