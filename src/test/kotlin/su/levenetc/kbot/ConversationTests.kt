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
                                        .then(EndMessage(endMessage)))))

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
        val userMessage = "ping"
        val botMessage = "pong"

        val model = waitForUserMessage(userMessage)
                .thenLast(botMessage)

        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
        val conversation = Conversation(model, outHandler)

        conversation.start()
        conversation.onUserMessage(userMessage)

        Mockito.verify(outHandler).send(botMessage)
        assertTrue(conversation.isFinished)
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