package su.levenetc.kbot.conversation

open class UserMessage() : Message() {
    constructor(message: String) : this() {
        this.message = message
    }

    constructor(validator: MessageValidator = EmptyMessageValidator()) : this() {
        this.validator = validator
    }

    constructor(message: String, validator: MessageValidator) : this() {
        this.message = message
        this.validator = validator
    }
}

fun waitForUserMessage(message: String): UserMessage {
    return UserMessage(message, ExactValidator(message))
}