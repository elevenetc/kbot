package su.levenetc.kbot.conversation

open class UserMessage() : Message() {
    constructor(message: String) : this() {
        this.message = message
    }

    constructor(validator: MessageValidator = EmptyMessageValidator()) : this() {
        this.validator = validator
    }
}