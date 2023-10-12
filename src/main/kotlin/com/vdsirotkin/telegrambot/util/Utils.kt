package com.vdsirotkin.telegrambot.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Update
import org.slf4j.MDC

val OBJECT_MAPPER = jacksonObjectMapper()

fun Update.determineChatId(): Long {
    return when {
        this.message() != null -> message().chat().id()
        this.callbackQuery() != null -> callbackQuery().from().id()
        this.inlineQuery() != null -> inlineQuery().from().id()
        this.chosenInlineResult() != null -> chosenInlineResult().from().id()
        else -> throw IllegalArgumentException("Unsupported message type. Update: $this")
    }
}

fun Update.isGroup(): Boolean {
    val type = this.message()?.chat()?.type() ?: return false
    return when (type) {
        Chat.Type.group, Chat.Type.supergroup, Chat.Type.channel -> true
        else -> false
    }
}

fun setupMdc(chatId: Long) {
    MDC.put(MDC_USER_ID, chatId.toString())
    MDC.put(MDC_CALL_ID, IdGenerator.uuid)
}
