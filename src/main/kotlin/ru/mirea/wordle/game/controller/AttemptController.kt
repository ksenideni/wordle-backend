package ru.mirea.wordle.game.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import ru.mirea.wordle.game.model.Progress
import ru.mirea.wordle.game.service.AttemptService

@RestController
@RequestMapping("/wordle/attempts")
class AttemptController(
    val attemptService: AttemptService,
    @Value("\${endpoints.cors.allowed-origins}")
    var corsAllowedOrigins: String
) {

    @GetMapping
    fun getAttempts(
        @RequestParam chatId: String,
        @RequestParam userId: String,
        response: HttpServletResponse
    ): Progress {
        response.addHeader("Access-Control-Allow-Origin", corsAllowedOrigins);
        return attemptService.getAttempts(chatId, userId)
    }

    @PostMapping
    fun postAttempt(
        @RequestParam chatId: String,
        @RequestParam userId: String,
        @RequestBody currentWord: String,
        response: HttpServletResponse
    ): Progress {
        response.addHeader("Access-Control-Allow-Origin", corsAllowedOrigins);
        return attemptService.postAttempt(chatId, userId, currentWord)
    }
}
