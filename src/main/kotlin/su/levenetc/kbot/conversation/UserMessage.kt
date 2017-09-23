package su.levenetc.kbot.conversation

open class UserMessage() : Message() {

    constructor(message: String) : this() {
        this.message = message
        this.validator = ExactValidator(message)
    }

    constructor(validator: MessageValidator = EmptyMessageValidator()) : this("", validator)

    constructor(message: String, validator: MessageValidator) : this() {
        this.message = message
        this.validator = validator
    }
}

fun message(message: String): Message {
    return UserMessage(message, ExactValidator(message))
}

fun anyUserMessage(): UserMessage {
    return UserMessage(EmptyMessageValidator())
}

fun waitForUserMessage(message: UserMessage): UserMessage {
    return message
}

fun waitForUserMessage(message: String): UserMessage {
    return UserMessage(message, ExactValidator(message))
}

fun positiveMessage(): Message {
    return UserMessage(PositiveValidator())
}

fun negativeMessage(): Message {
    return UserMessage(NegativeValidator())
}

class PositiveValidator : MessageValidator {

    override fun isValid(message: String): Boolean {
        var msg = message.toLowerCase().trim()
        return msg == "yes" || msg == "yeah"
    }

    override fun onError(message: String): String {
        return ""
    }

}

class NegativeValidator : MessageValidator {

    override fun isValid(message: String): Boolean {
        var msg = message.toLowerCase().trim()
        return msg == "no" || msg == "nope"
    }

    override fun onError(message: String): String {
        return ""
    }

}