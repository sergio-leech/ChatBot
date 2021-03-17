package com.example.chatbot.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.data.Message
import com.example.chatbot.databinding.MessageItemBinding
import com.example.chatbot.databinding.MessageItemBotBinding
import com.example.chatbot.utils.Constants.RECEIVE_ID
import com.example.chatbot.utils.Constants.SEND_ID

class MessagingAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> MessagingViewHolder(
                MessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> BotMessagingViewHolder(
                MessageItemBotBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as MessagingViewHolder).bind(getItem(position))
            1 -> (holder as BotMessagingViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).id) {
            SEND_ID -> 0
            else -> 1
        }
    }

  private  class MessagingViewHolder(private val _binding: MessageItemBinding) :
        RecyclerView.ViewHolder(_binding.root) {

        fun bind(_message: Message) {
            _binding.apply {
                message = _message
                executePendingBindings()
            }
        }
    }

   private class BotMessagingViewHolder(private val binding: MessageItemBotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(_message: Message) {
            binding.apply {
                message = _message
                executePendingBindings()
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }
    }
}