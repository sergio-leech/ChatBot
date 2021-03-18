package com.example.chatbot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.chatbot.databinding.ActivityMainBinding
import com.example.chatbot.ui.MessagingAdapter
import com.example.chatbot.view_model.Contract
import com.example.chatbot.view_model.MessageViewModel
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {
    lateinit var messagingAdapter: MessagingAdapter
    lateinit var binding: ActivityMainBinding
    private val messageViewModel: MessageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.etMessage.addTextChangedListener { message ->
            messageViewModel.onMessageChange(message.toString())
        }

        binding.btnSend.setOnClickListener {
            messageViewModel.setEvent(Contract.Event.OnSendMessage)
            messagingAdapter.notifyDataSetChanged()
            binding.rvMessages.smoothScrollToPosition(messagingAdapter.itemCount)
            binding.etMessage.setText("")
        }

        recyclerViewInit()
        sendMessage()

    }


    private fun recyclerViewInit() {
        messagingAdapter = MessagingAdapter()
        binding.rvMessages.adapter = messagingAdapter
    }

    private fun sendMessage() {
        lifecycleScope.launchWhenStarted {
            messageViewModel.uiState.collect {
                when (it.messagesList) {
                    is Contract.MessageListState.Idle -> {
                    }
                    is Contract.MessageListState.Loading -> {
                    }
                    is Contract.MessageListState.Success -> {
                        val messages = it.messagesList.list
                        messagingAdapter.submitList(messages)
                        messagingAdapter.notifyDataSetChanged()
                    }
                    is Contract.MessageListState.Error -> {
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            messageViewModel.effect.collect {effect->
                when(effect){
                    is Contract.Effect.ShowToast ->{
                        "Error".showToast()
                    }
                }
            }
        }
    }

    private fun String.showToast(){
        Toast.makeText(this@MainActivity,this,Toast.LENGTH_SHORT).show()
    }

}

