package com.vdsirotkin.telegrambot.bot

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.TelegramException
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.BaseResponse
import com.vdsirotkin.telegrambot.config.Resilience
import com.vdsirotkin.telegrambot.config.bot.BotConfigProps
import com.vdsirotkin.telegrambot.handler.BaseHandler
import com.vdsirotkin.telegrambot.handler.HandlerFactory
import com.vdsirotkin.telegrambot.handler.HandlerState
import com.vdsirotkin.telegrambot.handler.StatefulHandler
import com.vdsirotkin.telegrambot.localization.MessageSourceProvider
import com.vdsirotkin.telegrambot.util.*
import jakarta.annotation.PostConstruct
import mu.KLogging
import org.slf4j.MDC
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class RenameMeBot(
    private val props: BotConfigProps,
    private val handlerFactory: HandlerFactory,
    private val messageSourceProvider: MessageSourceProvider,
    private val resilience: Resilience,
    private val bot: TelegramBot
) {

    private val handlerStateMap: MutableMap<Long, HandlerState<*>> = mutableMapOf()

    @PostConstruct
    fun initUpdatesListener() {
        if (!props.enableUpdateListener) return
        bot.setUpdatesListener {
            it.filter {
                it.message() != null || it.callbackQuery() != null || it.inlineQuery() != null || it.chosenInlineResult() != null
            }.forEach {
                try {
                    onUpdateReceived(it)
                } catch (e: Exception) {
                    logger.error("Error while processing update: $it", e)
                }
            }
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }

    private fun onUpdateReceived(update: Update) {
        if (update.isGroup()) {
            handleGroup(update)
            return
        }
        val chatId = update.determineChatId()
        if (update.message()?.text() == "/cancel") {
            handleCancel(chatId)
            return
        }
        val handler: BaseHandler = when {
            update.message()?.text() == "/start" -> handlerFactory.startHandler
            handlerStateMap.containsKey(chatId) -> prepareStatefulHandler(chatId)
            else -> handlerFactory.unknownMessageHandler
        }

        try {
            setupMdc(chatId)
            handler.handle(this, update)
            if (handler is StatefulHandler<*>) {
                if (handler.state.finished) {
                    handlerStateMap.remove(chatId)
                } else {
                    handlerStateMap[chatId] = handler.state
                }
            }
        } catch (e: Exception) {
            logException(e)
            if (handler is StatefulHandler<*>) {
                handlerStateMap.remove(chatId)
            }
            sendErrorMessages(chatId)
        } finally {
            MDC.clear()
        }
    }

    private fun prepareStatefulHandler(chatId: Long): BaseHandler {
        val state = handlerStateMap[chatId]!!
        return prepareStatefulHandler(state)
    }

    @Suppress("UNCHECKED_CAST")
    private fun prepareStatefulHandler(state: HandlerState<*>): StatefulHandler<Any> {
        return (handlerFactory.newHandler(Class.forName(state.handlerClass).kotlin as KClass<out BaseHandler>) as StatefulHandler<Any>).apply {
            this.state = state as HandlerState<Any>
        }
    }

    private fun handleGroup(update: Update) {
        bot.execute(SendMessage(update.message().chat().id(), GROUP_MESSAGE))
    }

    private fun handleCancel(chatId: Long) {
        val messageSource = messageSourceProvider.getMessageSource(chatId)
        if (handlerStateMap.containsKey(chatId)) {
            val state = handlerStateMap.remove(chatId)!!
            val handler = prepareStatefulHandler(state)
            handler.cancel(this, chatId)
            executeSafe(SendMessageWithAction(chatId, messageSource["cancel.success"], "CANCEL").replyMarkup(
                ReplyKeyboardRemove()
            ))
        } else {
            executeSafe(SendMessageWithAction(chatId, messageSource["cancel.nothing"], "NOTHING_TO_CANCEL").replyMarkup(
                ReplyKeyboardRemove()
            ))
        }
    }

    private fun logException(t: Throwable) {
        if (t is TelegramException) {
            logger.error("Telegram api error: ${t.response()}, code: ${t.response().errorCode()}", t)
        } else {
            logger.error("Error occurred, message: ${t.message}", t)
        }
    }

    private fun sendErrorMessages(chatId: Long) {
        try {
            val messageSource = messageSourceProvider.getMessageSource(chatId)
            executeSafe(SendMessageWithAction(chatId, messageSource["error"], "ERROR").replyMarkup(ReplyKeyboardRemove()))
            props.serviceAccounts.forEach {
                executeSafe(SendMessage(it, "Error occurred, check call id ${MDC.get(MDC_CALL_ID)}"))
            }
        } catch (e: Exception) {
            logger.error("Unrecoverable error, message: ${e.message}", e)
        }
    }

    fun <T : BaseRequest<T, R>, R : BaseResponse> executeSafe(method: T): R {
        return with(resilience) {
            retry.executeSupplier {
                rateLimiter.executeSupplier {
                    bot.execute(method)
                }
            }
        }
    }

    companion object : KLogging() {
        const val GROUP_MESSAGE = "Sorry, i don't support groups. Please remove me from this chat."
    }

}
