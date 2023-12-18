package com.vdsirotkin.telegrambot.handler.common

import com.pengrad.telegrambot.model.Update
import com.vdsirotkin.telegrambot.bot.Bot
import com.vdsirotkin.telegrambot.handler.BaseHandler
import com.vdsirotkin.telegrambot.localization.MessageSourceProvider
import com.vdsirotkin.telegrambot.user.Language
import com.vdsirotkin.telegrambot.user.UserEntity
import com.vdsirotkin.telegrambot.user.UsersRepository
import com.vdsirotkin.telegrambot.util.SendMessageWithAction
import org.springframework.stereotype.Component

@Component
class StartHandler(
    private val messageSourceProvider: MessageSourceProvider,
    private val usersRepository: UsersRepository,
) : BaseHandler {

    override fun handle(bot: Bot, update: Update) {
        val chatId = update.message().chat().id()

        val telegramLanguageCode = update.message().from().languageCode()
        val language = Language.getByCode(telegramLanguageCode)

        if (!usersRepository.existsById(chatId)) {
            usersRepository.save(UserEntity(chatId, language))
        }

        val messageSource = messageSourceProvider.getMessageSource(language)
        bot.executeSafe(SendMessageWithAction(chatId, messageSource["start.message"], action))
    }

    override val action: String
        get() = "START"
}
