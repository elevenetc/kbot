package su.levenetc.kbot

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import su.levenetc.kbot.conversation.Conversation
import su.levenetc.kbot.conversation.ConversationModel
import su.levenetc.kbot.models.Event
import su.levenetc.kbot.models.Message
import su.levenetc.kbot.models.RtmState
import su.levenetc.kbot.network.http.buildHttpApi
import su.levenetc.kbot.network.socket.WebSocketClient
import su.levenetc.kbot.utils.fatalStop


class KBot(private val token: String) {

    private val httpApi = buildHttpApi()
    private lateinit var socketClient: WebSocketClient

    init {
        if (token.isEmpty()) fatalStop("No defined Slack token")
    }

    fun start(callBack: InitCallback) {
        val rtmState = httpApi.rtmConnect(token)
        rtmState.enqueue(object : Callback<RtmState> {
            override fun onFailure(call: Call<RtmState>?, t: Throwable?) {
                callBack.onError()
            }

            override fun onResponse(call: Call<RtmState>?, response: Response<RtmState>) {
                val rtmState = response.body()

                if (rtmState != null) {
                    println(rtmState.url)
                    socketClient = WebSocketClient(rtmState.url)
                    socketClient.connect()
                    callBack.onSuccess(socketClient.eventsObservable(), object : Writer {
                        override fun write(msg: String) {
                            socketClient.write(msg)
                        }
                    })
                }


            }

        })
    }

    class ConversationCallback(private val model: ConversationModel) : InitCallback {

        private val userConversations = mutableMapOf<String, Conversation>()

        override fun onError() {

        }

        override fun onSuccess(eventsObservable: Observable<Event>, writer: Writer) {
            eventsObservable.filter({
                it is Message
            }).subscribe {

                val message = it as Message
                val userId = message.user
                val conversation: Conversation

                conversation = if (userConversations.containsKey(userId)) {
                    userConversations.getValue(userId)
                } else {
                    val newConversation = Conversation(model, SlackOutMessageHandler(message.channel, writer)).start()
                    userConversations.put(userId, newConversation)
                    newConversation
                }

                conversation.onUserMessage(message.text)
                if (conversation.isFinished) userConversations.remove(userId)
            }
        }

    }

    interface InitCallback {
        fun onError()
        fun onSuccess(eventsObservable: Observable<Event>, writer: Writer)
    }

    interface Writer {
        fun write(msg: String)
    }

}