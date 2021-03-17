package com.example.chatbot.view_model

import com.example.chatbot.data.Message

/**Contract of Activity*/
class Contract {

    /** Events that user performed*/
    sealed class Event : UiEvent {
        object OnSendMessage : Event()
    }

    /** Ui View States*/
    data class State(
        val messagesList: MessageListState
    ) : UiState

    /** View State that related to Gifs List*/
    sealed class MessageListState {
        object Idle : MessageListState()
        object Loading : MessageListState()
        data class Success(val list:List<Message>) : MessageListState()
        data class Error(val error: String?):MessageListState()
    }

    /** Side effects*/
    sealed class Effect : UiEffect {
        object ShowToast : Effect()
    }
}