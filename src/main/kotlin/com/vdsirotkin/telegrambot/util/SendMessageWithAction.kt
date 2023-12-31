package com.vdsirotkin.telegrambot.util

import com.pengrad.telegrambot.request.SendMessage

class SendMessageWithAction(chatId: Any, text: String, val action: String) : SendMessage(chatId, text) {

    override fun getMethod(): String {
        return "sendMessage"
    }
}
