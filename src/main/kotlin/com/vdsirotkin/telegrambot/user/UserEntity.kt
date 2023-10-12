package com.vdsirotkin.telegrambot.user

import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    val chatId: Long,
    @Enumerated(STRING)
    val language: Language,
    @Version
    val version: Int = 0,
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    val createdAt: Instant? = null,
    @UpdateTimestamp
    @Column(nullable = false)
    val updatedAt: Instant? = null,
)

enum class Language(val code: String, val locale: Locale) {
    RU("ru", Locale.forLanguageTag("ru")),
    EN("en", Locale.forLanguageTag("en"));

    companion object {
        fun getByCode(code: String): Language {
            return Language.values().find { it.code == code } ?: EN
        }
    }
}
