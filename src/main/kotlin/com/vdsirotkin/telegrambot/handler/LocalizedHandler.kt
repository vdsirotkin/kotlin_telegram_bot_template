package com.vdsirotkin.telegrambot.handler

import com.pengrad.telegrambot.model.Update
import com.vdsirotkin.telegrambot.bot.RenameMeBot
import com.vdsirotkin.telegrambot.localization.MessageSourceProvider
import com.vdsirotkin.telegrambot.localization.MessageSourceWrapper
import com.vdsirotkin.telegrambot.util.determineChatId
import mu.KotlinLogging

interface LocalizedHandler : BaseHandler {

    val messageSourceProvider: MessageSourceProvider

    override fun handle(bot: RenameMeBot, update: Update) {
        KotlinLogging.logger(LocalizedHandler::class.java.name).info("Executing ${this@LocalizedHandler.javaClass.name}")
        messageSourceProvider.getMessageSource(update.determineChatId()).apply {
            handleInternal(bot, update)
        }
    }

    fun MessageSourceWrapper.handleInternal(bot: RenameMeBot, update: Update)
}
