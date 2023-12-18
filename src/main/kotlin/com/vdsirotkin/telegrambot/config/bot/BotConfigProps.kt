package com.vdsirotkin.telegrambot.config.bot

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "bot")
data class BotConfigProps @ConstructorBinding constructor(
    @field:NotBlank
    val token: String,
    val serviceAccounts: List<Long>,
    @DefaultValue("true")
    val enableUpdateListener: Boolean,
    @DefaultValue("false")
    val testEnv: Boolean
)
