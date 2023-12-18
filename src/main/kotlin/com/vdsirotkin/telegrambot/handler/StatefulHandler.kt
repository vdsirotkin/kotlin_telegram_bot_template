package com.vdsirotkin.telegrambot.handler

import com.vdsirotkin.telegrambot.bot.Bot
import mu.KotlinLogging
import org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

interface StatefulHandler<DATA> : BaseHandler {

    var state: HandlerState<DATA>

    fun cancel(bot: Bot, chatId: Long) {
        KotlinLogging.logger(StatefulHandler::class.java.name).info("Nothing to cancel")
    }
}

@Service
@Scope(SCOPE_PROTOTYPE)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class PrototypeService

interface HandlerState<DATA> {
    val finished: Boolean
    val handlerClass: String
    val data: DATA
}
