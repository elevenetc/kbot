package su.levenetc.kbot.conversation

class UserMessage() : Message() {
    constructor(message: String) : this() {
        this.message = message
    }

    constructor(validator: MessageValidator = EmptyMessageValidator()) : this() {
        this.validator = validator
    }
}