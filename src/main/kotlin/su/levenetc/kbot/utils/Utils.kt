package su.levenetc.kbot.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode

fun getField(json: String): String {
    val node = ObjectMapper().readValue(json.toByteArray(), ObjectNode::class.java)
    return node.get("type").textValue()
}