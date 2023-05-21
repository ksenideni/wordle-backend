package ru.mirea.wordle.game.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mirea.wordle.game.model.Progress
import ru.mirea.wordle.game.service.AttemptService

@RestController
@RequestMapping("/wordle/attempts")
class AttemptController(val attemptService: AttemptService) {

    @GetMapping("/{chatId}/{userId}")
    fun getAttempts(chatId: String, userId: String): Progress {
        return attemptService.getAttempts(chatId, userId)
    }

    @PostMapping("/{chatId}/{userId}")
    fun postAttempt(chatId: String, userId: String, currentWord: String): Progress {
        return attemptService.postAttempt(chatId, userId, currentWord)
    }
}
