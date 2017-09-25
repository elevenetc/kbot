package su.levenetc.kbot

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import su.levenetc.kbot.conversation.*

class ConversationTests {

    private lateinit var outHandler: OutMessagesHandler

    @Before
    fun before() {
        outHandler = Mockito.mock(OutMessagesHandler::class.java)
    }

    @Test
    fun simpleBotMessage() {
        val model = initFromBotMessage(hello).andFinish().build()
        val conversation = Conversation(model, outHandler)

        assertTrue(conversation.start().isFinished)
        Mockito.verify(outHandler).send(hello)
    }

    @Test
    fun simpleUserMessage() {
        val model = waitForUserMessage(hello).andFinish().build()
        val conversation = Conversation(model, outHandler)

        assertTrue(
                conversation.start().onUserMessage(hello).isFinished
        )
    }

    @Test
    fun testList() {

        val q1 = "Hi, how are you?"
        val q2 = "Ok. How old are?"
        val endMessage = "Cool. Bye!"

        val model = initFromBotMessage(q1)
                .then(anyUserMessage()
                        .then(BotMessage(q2)
                                .then(anyUserMessage()
                                        .andFinish(endMessage)))).build()

        val outHandler = Mockito.mock(OutMessagesHandler::class.java)
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
                                .andFinish("Congrats!")
                )
                .build()

        val outHandler = Mockito.mock(OutMessagesHandler::class.java)
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
                .andFinish(pongMessage)
                .build()

        val outHandler = Mockito.mock(OutMessagesHandler::class.java)
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
                                                        positiveMessage().andFinish("That's bad! Bye X!"),
                                                        negativeMessage().andFinish("You're lucky! Good bye X!")
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
                                                                                        positiveMessage().andFinish("Cool! I'll send you email! Bye!"),
                                                                                        negativeMessage().andFinish("No problem! Bye!")
                                                                                )
                                                                ),
                                                        negativeMessage().andFinish("Cool. Good bye Y!"))
                                )
                )
                .build()

        val conversation = Conversation(model, outHandler)

        conversation.start()
        conversation.onUserMessage("Y")
        conversation.onUserMessage("yes")
        conversation.onUserMessage("no")

        Mockito.verify(outHandler).send("No problem! Bye!")
        assertTrue(conversation.isFinished)
    }

    @Test
    fun testSimpleSurveyReport() {
        val model = initFromBotMessage(howAreYou)
                .then(
                        anyUserMessage().andFinish(okBye)
                )
                .build()

        val conversation = Conversation(model, outHandler)
        conversation.start()
        conversation.onUserMessage(fine)

        assertEquals(howAreYou, conversation.log[0].message)
        assertEquals(fine, conversation.log[1].message)
        assertEquals(okBye, conversation.log[2].message)
    }

    @Test
    fun testTreeSurveyReport() {
        val model = initFromBotMessage(aOrB)
                .thenOneOf(
                        message(a).andFinish(endA),
                        message(b).andFinish(endB)
                ).build()

        val conversation = Conversation(model, outHandler)
        conversation.start()
        conversation.onUserMessage(a)

        assertEquals(aOrB, conversation.log[0].message)
        assertEquals(a, conversation.log[1].message)
        assertEquals(endA, conversation.log[2].message)
    }

    @Test
    fun testDialog() {
        val model = waitForUserMessage(messageA)
                .then(BotMessage(messageB)
                        .then(
                                UserMessage(messageC)
                                        .andFinish(messageD)
                        )
                )
                .build()

        val conversation = Conversation(model, outHandler)
        conversation.start()
        conversation.onUserMessage(messageA)
        conversation.onUserMessage(messageC)

        assertEquals(messageA, conversation.log[0].message)
        assertEquals(messageB, conversation.log[1].message)
        assertEquals(messageC, conversation.log[2].message)
        assertEquals(messageD, conversation.log[3].message)
    }

    @Test
    fun testActOnMessage() {

        val action = Mockito.mock(MessageAction::class.java)


        val model = waitForUserMessage(load)
                .then(BotMessage(questionID)
                        .then(actOnUserMessage(action)
                                .then(BotMessage(result))
                        )
                )
                .build()

        val conversation = Conversation(model, outHandler)
        conversation.start()
        conversation.onUserMessage(load)
        conversation.onUserMessage(id1)

        Mockito.verify(action).act(id1)

        assertEquals(load, conversation.log[0].message)
        assertEquals(questionID, conversation.log[1].message)
        assertEquals(id1, conversation.log[2].message)
        assertEquals(result, conversation.log[3].message)
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