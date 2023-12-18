package com.vdsirotkin.telegrambot.handler

import com.pengrad.telegrambot.model.Update
import com.vdsirotkin.telegrambot.bot.Bot

interface BaseHandler {

    val action: String

    fun handle(bot: Bot, update: Update)

}
