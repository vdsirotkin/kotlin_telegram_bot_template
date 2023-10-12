package com.vdsirotkin.telegrambot.localization

import com.vdsirotkin.telegrambot.user.Language
import com.vdsirotkin.telegrambot.user.UsersRepository
import org.springframework.context.MessageSource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MessageSourceProvider(
    private val messageSource: MessageSource,
    private val usersRepository: UsersRepository,
) {

    fun getMessageSource(chatId: Long): MessageSourceWrapper {
        val user = usersRepository.findByIdOrNull(chatId) ?: throw IllegalArgumentException("Can't find user with chat id = '$chatId' in database")
        return MessageSourceWrapper(messageSource, user.language.locale)
    }

    fun getMessageSource(language: Language): MessageSourceWrapper = MessageSourceWrapper(messageSource, language.locale)
}
