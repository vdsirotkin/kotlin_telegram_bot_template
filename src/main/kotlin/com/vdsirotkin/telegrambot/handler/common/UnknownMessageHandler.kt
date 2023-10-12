package com.vdsirotkin.telegrambot.handler.common

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.vdsirotkin.telegrambot.bot.RenameMeBot
import com.vdsirotkin.telegrambot.handler.LocalizedHandler
import com.vdsirotkin.telegrambot.localization.MessageSourceProvider
import com.vdsirotkin.telegrambot.localization.MessageSourceWrapper
import com.vdsirotkin.telegrambot.util.SendMessageWithAction
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class UnknownMessageHandler(override val messageSourceProvider: MessageSourceProvider) : LocalizedHandler {

    override fun MessageSourceWrapper.handleInternal(bot: RenameMeBot, update: Update) {
        when {
            update.message() != null -> {
                logger.info("Received some spam: ${update.message().text()}")
                bot.executeSafe(SendMessageWithAction(update.message().chat().id(), getText("unknown.text"), action).replyMarkup(ReplyKeyboardRemove()))
            }
            update.callbackQuery() != null -> {
                logger.info("Received dangling inline query")
                bot.executeSafe(AnswerCallbackQuery(update.callbackQuery().id()).text(getText("unknown.text")))
            }
            else -> {}
        }
    }

    override val action: String
        get() = "UNKNOWN_MESSAGE"

    companion object : KLogging()
}
