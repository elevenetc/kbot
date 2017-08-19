package su.levenetc.kbot

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import su.levenetc.kbot.conversation.ConvRunner
import su.levenetc.kbot.conversation.Conversation
import su.levenetc.kbot.conversation.MessageValidator
import su.levenetc.kbot.conversation.OutBotMessagesHandler

class TestConversations {
    @Test
    fun testPingPong() {

        val conversation = Conversation
                .waitFor("ping")
                .respondWith("pong")
                .end()
        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
        val runner = ConvRunner(conversation, outHandler)

        runner.start()
        runner.onUserMessage("ping")
        `verify`(outHandler, Mockito.atLeastOnce()).send("pong")
        assertTrue(runner.isFinished)
    }

    @Test
    fun testMath() {

        val question = "1+1=?"
        val nullOrEmpty = "solution should not be empty"
        val justInvalid = "invalid solution"

        val conversation = Conversation
                .startFrom(question)
                .expect(OnePlusOne(nullOrEmpty, justInvalid))
                .end()

        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
        val runner = ConvRunner(conversation, outHandler)

        runner.start()
        runner.onUserMessage("1")
        assertFalse(runner.isFinished)
        runner.onUserMessage("2")
        assertTrue(runner.isFinished)

        `verify`(outHandler).send(question)
        `verify`(outHandler).send(justInvalid)

    }

    class OnePlusOne(private val nullOrEmpty: String,
                     private val justInvalid: String) : MessageValidator {

        override fun onError(message: String): String {
            return if (message.isEmpty()) {
                nullOrEmpty
            } else {
                justInvalid
            }
        }

        override fun isValid(message: String): Boolean {
            if (message.isEmpty()) return false
            if (message.length > 1) return false
            return message == "2"
        }

    }
}