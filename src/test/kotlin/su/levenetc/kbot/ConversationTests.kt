package su.levenetc.kbot

import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import su.levenetc.kbot.conversation.*

class TestConversations {

//    @Test
//    fun testList() {
//        val conversation = Conversation
//                .initWith("q1")
//                .getResponseAndSend("q2")
//                .getResponseAndSend("q3")
//                .getResponseAndEnd()
//
//        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
//        val runner = ConversationRunner(conversation, outHandler)
//
//        runner.start()
//        runner.onUserMessage("a1")
//        runner.onUserMessage("a2")
//        runner.onUserMessage("a3")
//
//        `verify`(outHandler, Mockito.atLeastOnce()).send("q1")
//        `verify`(outHandler, Mockito.atLeastOnce()).send("q2")
//        `verify`(outHandler, Mockito.atLeastOnce()).send("q3")
//        assertTrue(runner.isFinished)
//    }
//
//    @Test
//    fun testPingPong() {
//
//        val conversation = Conversation
//                .waitFor("ping")
//                .respondWith("pong")
//                .end()
//        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
//        val runner = ConversationRunner(conversation, outHandler)
//
//        runner.start()
//        runner.onUserMessage("ping")
//        `verify`(outHandler, Mockito.atLeastOnce()).send("pong")
//        assertTrue(runner.isFinished)
//    }
//
//    @Test
//    fun testMath() {
//
//        val question = "1+1=?"
//        val nullOrEmpty = "solution should not be empty"
//        val justInvalid = "invalid solution"
//
//        val conversation = Conversation
//                .initWith(question)
//                .expect(OnePlusOne(nullOrEmpty, justInvalid))
//                .end()
//
//        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
//        val runner = ConversationRunner(conversation, outHandler)
//
//        runner.start()
//        runner.onUserMessage("1")
//        assertFalse(runner.isFinished)
//        runner.onUserMessage("2")
//        assertTrue(runner.isFinished)
//
//        `verify`(outHandler).send(question)
//        `verify`(outHandler).send(justInvalid)
//        assertTrue(runner.isFinished)
//
//    }

    @Test
    fun testList() {
        val q1 = "Hi, how are you?"
        val q2 = "Ok. How old are?"
        val endMessage = "Cool. Bye!"
        val root = initFromBotMessage(q1)
                .then(anyUserMessage()
                        .then(BotMessage(q2)
                                .then(anyUserMessage()
                                        .then(EndMessage(endMessage)))))

        val outHandler = Mockito.mock(OutBotMessagesHandler::class.java)
        val runner = ConversationRunner(root, outHandler)
        runner.start()
        runner.onUserMessage("Hi! Nice!")
        runner.onUserMessage("14")

        Mockito.verify(outHandler).send(q1)
        Mockito.verify(outHandler).send(q2)
        Mockito.verify(outHandler).send(endMessage)
        assertTrue(runner.isFinished)
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