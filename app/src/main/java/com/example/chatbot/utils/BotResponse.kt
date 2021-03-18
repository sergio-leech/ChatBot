package com.example.chatbot.utils

import android.annotation.SuppressLint


object BotResponse {
    @SuppressLint("DefaultLocale")
    fun basicResponses(_message: String): String {
        val random = (0..2).random()
        val message = _message.toLowerCase()

        return when {
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello there"
                    1 -> "Sup"
                    2 -> "Hi "
                    else -> ""
                }
            }
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine, thanks for asking"
                    1 -> "I'm hungry"
                    2 -> "Pretty good. How about you?"
                    else -> ""
                }
            }
            message.contains("flip") && message.contains("coin") -> {
                val randomNum = (0..1).random()
                val result = if (randomNum == 0) "heads" else "tails"
                "I flipped a coin and it landed on $result"
            }
            message.contains("solve") -> {
                val equation: String? = message.substringAfter("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    answer.toString()
                } catch (e: Exception) {
                    "Sorry, I can't solve that"
                }
            }
            message.contains("time") && message.contains("?") -> {
                Time.timeStamp()
            }

            else -> {
                when (random) {
                    0 -> "I don't understand..."
                    1 -> "IDK"
                    2 -> "Try asking me something different"
                    else -> ""
                }
            }
        }
    }

}