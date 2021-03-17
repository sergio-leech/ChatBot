package com.example.chatbot

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.chatbot.data.Message
import com.example.chatbot.databinding.ActivityMainBinding
import com.example.chatbot.ui.MessagingAdapter
import com.example.chatbot.utils.Constants.OPEN_GOOGLE
import com.example.chatbot.utils.Constants.OPEN_SEARCH
import com.example.chatbot.view_model.Contract
import com.example.chatbot.view_model.MessageViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var messagingAdapter: MessagingAdapter
    lateinit var binding: ActivityMainBinding
    private val messageViewModel: MessageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            val response = messageViewModel.addNewMessage(message)
            botResponse(response)
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
                    }
                    is Contract.MessageListState.Error->{}
                }
            }
        }
    }

    private fun botResponse(response: String) {

        when (response) {
            OPEN_GOOGLE -> {
                val site = Intent(Intent.ACTION_VIEW)
                site.data = Uri.parse("https://www.google.com/")
                startActivity(site)
            }
            OPEN_SEARCH -> {
                val site = Intent(Intent.ACTION_VIEW)
                val searchTerm: String? = response.substringAfterLast("search")
                site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                startActivity(site)
            }

        }
    }

}

