package com.vdsirotkin.telegrambot.handler

import com.pengrad.telegrambot.model.Update
import com.vdsirotkin.telegrambot.bot.RenameMeBot

interface BaseHandler {

    val action: String

    fun handle(bot: RenameMeBot, update: Update)

}
