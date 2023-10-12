package com.vdsirotkin.telegrambot

import com.vdsirotkin.telegrambot.config.bot.BotConfigProps
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(BotConfigProps::class)
class TelegramBotApplication

fun main(args: Array<String>) {
    runApplication<TelegramBotApplication>(*args)
}
