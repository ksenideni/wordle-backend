package ru.mirea.wordle.game.service

import org.springframework.stereotype.Service
import ru.mirea.wordle.game.model.Progress
import ru.mirea.wordle.storage.redis.RedisTargetWordStorage
import ru.mirea.wordle.storage.redis.RedisUserStorage

@Service
class AttemptService(
    val wordleService: WordleService,
    val userStorage: RedisUserStorage,
    val wordStorage: RedisTargetWordStorage
) {
    fun getAttempts(chatId: String, userId: String): Progress {
        val user = userStorage.getUser(chatId, userId)
        return user.progress

    }

    fun postAttempt(chatId: String, userId: String, currentWord: String): Progress{
        val user = userStorage.getUser(chatId, userId)
        val targetWord = wordStorage.getTargetWordForUser(user)
        val progress = wordleService.makeTry(user.progress, currentWord, targetWord)
        return progress
    }
}