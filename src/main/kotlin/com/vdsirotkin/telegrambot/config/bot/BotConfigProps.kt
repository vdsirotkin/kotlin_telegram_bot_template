package com.vdsirotkin.telegrambot.config.bot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties(prefix = "bot")
data class BotConfigProps @ConstructorBinding constructor(
    val token: String,
    val serviceAccounts: List<Long>,
    @DefaultValue("true")
    val enableUpdateListener: Boolean,
    @DefaultValue("false")
    val testEnv: Boolean
)
