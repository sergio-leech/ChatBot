package com.example.chatbot.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chatbot.data.Message
import com.example.chatbot.utils.BotResponse
import com.example.chatbot.utils.Constants
import com.example.chatbot.utils.Time
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MessageViewModel:BaseViewModel<Contract.State,Contract.Event,Contract.Effect>() {
       private val listMessage = mutableListOf(Message("Hello! How may I help",Constants.RECEIVE_ID,Time.timeStamp()))

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
        when(event){
            is Contract.Event.OnSendMessage -> {

            }
        }
    }

init {

    setListMessage()
}
   private fun setListMessage(){
       viewModelScope.launch {
           setState { copy(messagesList = Contract.MessageListState.Loading) }
           try {

               setState { copy(messagesList = Contract.MessageListState.Success(listMessage)) }
           }catch (e:Exception){
               setState { copy(messagesList = Contract.MessageListState.Error(e.toString())) }

           }
       }

    }


 fun addNewMessage(newMessage:String):String{

        if (newMessage.isNotEmpty()){
            val message = Message(newMessage,Constants.SEND_ID,Time.timeStamp())
            listMessage.add(message)
        }

        return chatBotResponse(newMessage)
    }

   private fun chatBotResponse(message:String):String{
       val response = BotResponse.basicResponses(message)
        listMessage.add(Message(response,Constants.RECEIVE_ID,Time.timeStamp()))
       return response
    }





}