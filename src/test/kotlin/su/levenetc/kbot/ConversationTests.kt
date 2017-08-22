package su.levenetc.kbot

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import su.levenetc.kbot.conversation.*

class TestConversations {

    @Test
    fun testList() {

        val q1 = "Hi, how are you?"
        val q2 = "Ok. How old are?"
        val endMessage = "Cool. Bye!"

        val model = initFromBotMessage(q1)
                .then(anyUserMessage()
                        .then(BotMessage(q2)
                                .then(anyUserMessage()
                                        .thenLast(endMessage))))

        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
        val conversation = Conversation(model, outHandler)
        conversation.start()
        conversation.onUserMessage("Hi! Nice!")
        conversation.onUserMessage("14")

        Mockito.verify(outHandler).send(q1)
        Mockito.verify(outHandler).send(q2)
        Mockito.verify(outHandler).send(endMessage)
        assertTrue(conversation.isFinished)
    }

    @Test
    fun testSum() {
        val question = "1+1=?"
        val invalidMessage = "Nope, try again."
        val model = initFromBotMessage(question)
                .then(
                        UserMessage(OnePlusOneValidator(invalidMessage))
                                .thenLast("Congrats!")
                )

        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
        val conversation = Conversation(model, outHandler)

        conversation.start()

        conversation.onUserMessage("1")
        assertFalse(conversation.isFinished)
        conversation.onUserMessage("2")
        assertTrue(conversation.isFinished)

        Mockito.verify(outHandler).send(question)
        Mockito.verify(outHandler).send(invalidMessage)
    }

    @Test
    fun testPingPong() {
        val pingMessage = "ping"
        val pongMessage = "pong"

        val model = waitForUserMessage(pingMessage)
                .thenLast(pongMessage)

        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
        val conversation = Conversation(model, outHandler)

        conversation.start()
        conversation.onUserMessage(pingMessage)

        Mockito.verify(outHandler).send(pongMessage)
        assertTrue(conversation.isFinished)
    }

    @Test
    fun testTree() {

        val model = initFromBotMessage("Are you X or Y?")
                .thenOneOf(
                        UserMessage("X")
                                .then(
                                        BotMessage("Hello X. Are you older than 50?")
                                                .thenOneOf(
                                                        positiveMessage().thenLast("That's bad! Bye X!"),
                                                        negativeMessage().thenLast("You're lucky! Good bye X!")
                                                )
                                ),
                        UserMessage("Y")
                                .then(
                                        BotMessage("Hello Y. Do you like Z?")
                                                .thenOneOf(
                                                        positiveMessage()
                                                                .then(
                                                                        BotMessage("Do you want to buy Z?")
                                                                                .thenOneOf(
                                                                                        positiveMessage().thenLast("Cool! I'll send you email! Bye!"),
                                                                                        negativeMessage().thenLast("No problem! Bye!")
                                                                                )
                                                                ),
                                                        negativeMessage().thenLast("Cool. Good bye Y!"))

                                )
                )

        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
        val conversation = Conversation(model, outHandler)

        conversation.start()
        conversation.onUserMessage("Y")
        conversation.onUserMessage("yes")
        conversation.onUserMessage("no")

        Mockito.verify(outHandler).send("No problem! Bye!")
        assertTrue(conversation.isFinished)
    }

    @Test
    fun testNoValidVariants() {

        val q1 = "What time is it now?"
        val q2 = "How old are you?"
        val a1 = "10:00"
        val a2 = "50"
        val a3 = "Have no idea, sorry"

        waitForUserMessage(anyUserMessage())
                .thenLast(BotMessage(object : MessageHandler {
                    override fun get(userMessage: String): String {
                        if (userMessage == q1) {
                            return a1
                        } else if (userMessage == q2) {
                            return a2
                        } else {
                            return a3
                        }
                    }
                }))
    }

    class OnePlusOneValidator(private val invalidMessage: String) : MessageValidator {

        override fun onError(message: String): String {
            return invalidMessage
        }

        override fun isValid(message: String): Boolean {
            if (message.isEmpty()) return false
            if (message.length > 1) return false
            return message == "2"
        }

    }
}