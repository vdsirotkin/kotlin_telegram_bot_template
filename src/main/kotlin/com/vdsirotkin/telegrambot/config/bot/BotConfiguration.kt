package com.vdsirotkin.telegrambot.config.bot

import com.pengrad.telegrambot.TelegramBot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfiguration {

    @Bean
    fun telegramBot(props: BotConfigProps): TelegramBot {
        return TelegramBot.Builder(props.token).useTestServer(props.testEnv).build()
    }
}
