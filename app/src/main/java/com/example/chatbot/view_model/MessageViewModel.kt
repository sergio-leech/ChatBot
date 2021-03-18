package com.example.chatbot.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chatbot.data.Message
import com.example.chatbot.utils.BotResponse
import com.example.chatbot.utils.Constants
import com.example.chatbot.utils.Time
import kotlinx.coroutines.launch

@Suppress("NAME_SHADOWING")
class MessageViewModel : BaseViewModel<Contract.State, Contract.Event, Contract.Effect>() {
    private val listMessage = mutableListOf(
        Message(
            "Hello! How may I help",
            Constants.RECEIVE_ID, Time.timeStamp()
        )
    )

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    fun onMessageChange(newMessage: String) {
        _message.value = newMessage
    }

    /**
     * Create initial State of Views
     */
    override fun createInitialState(): Contract.State {
        return Contract.State(Contract.MessageListState.Idle)
    }

    /**
     * Handle each event
     */
    override fun handleEvent(event: Contract.Event) {
        when (event) {
            is Contract.Event.OnSendMessage -> {
                addNewMessage()
            }
        }
    }

    init {

        setListMessage()
    }

    private fun setListMessage() {
        viewModelScope.launch {
            setState { copy(messagesList = Contract.MessageListState.Loading) }
            try {
                setState { copy(messagesList = Contract.MessageListState.Success(listMessage)) }
            } catch (e: Exception) {
                setState { copy(messagesList = Contract.MessageListState.Error(e.toString())) }
                setEffect { Contract.Effect.ShowToast }
            }
        }
    }


    private fun addNewMessage() {
        val newMessage = _message.value
        val message = Message(newMessage ?: "", Constants.SEND_ID, Time.timeStamp())
        listMessage.add(message)
        chatBotResponse(newMessage ?: "")
    }

    private fun chatBotResponse(message: String) {
        val response = BotResponse.basicResponses(message)
        listMessage.add(Message(response, Constants.RECEIVE_ID, Time.timeStamp()))
    }

}