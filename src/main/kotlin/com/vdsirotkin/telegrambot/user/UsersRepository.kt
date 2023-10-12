package com.vdsirotkin.telegrambot.user

import org.springframework.data.jpa.repository.JpaRepository

interface UsersRepository : JpaRepository<UserEntity, Long>
