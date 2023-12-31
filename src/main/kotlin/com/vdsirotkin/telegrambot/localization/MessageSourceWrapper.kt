package com.vdsirotkin.telegrambot.localization

import org.springframework.context.MessageSource
import java.util.*

class MessageSourceWrapper(
    private val messageSource: MessageSource,
    private val locale: Locale
) {

    operator fun get(code: String): String = messageSource.getMessage(code, null, locale)

    fun getText(code: String) = messageSource.getMessage(code, null, locale)

}
