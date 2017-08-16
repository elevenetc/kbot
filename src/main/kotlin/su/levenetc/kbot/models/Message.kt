package su.levenetc.kbot.models

/**
 * Created by eugene.levenetc on 14/07/2017.
 */
class Message : Event() {
    var channel: String = ""
    var user: String = ""
    var text: String = ""
    var source_team: String = ""
    var team: String = ""
}