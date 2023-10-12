package com.vdsirotkin.telegrambot.handler

import com.vdsirotkin.telegrambot.handler.common.StartHandler
import com.vdsirotkin.telegrambot.handler.common.UnknownMessageHandler
import org.springframework.beans.factory.annotation.Lookup
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
abstract class HandlerFactory(
        private val applicationContext: ApplicationContext
) {

    @get:Lookup
    abstract val startHandler: StartHandler

    @get:Lookup
    abstract val unknownMessageHandler: UnknownMessageHandler

    fun newHandler(kClass: KClass<out BaseHandler>): BaseHandler = applicationContext.getBean(kClass.java)
}
