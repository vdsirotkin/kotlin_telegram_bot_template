package com.vdsirotkin.telegrambot.util

import java.math.BigInteger
import java.security.SecureRandom
import java.util.*

object IdGenerator {

    private val random = SecureRandom()

    val id: String
        get() = BigInteger(80, random).toString(32)

    val uuid: String
        get() = UUID.randomUUID().toString()
}
