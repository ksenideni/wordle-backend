package ru.mirea.wordle.telegram

import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.mirea.wordle.WordleApplication
import ru.mirea.wordle.telegram.model.GetScoresResponse
import ru.mirea.wordle.telegram.model.SetScoreResponse

@Service
class TelegramServiceImpl(
    private val webClient: WebClient,
) : TelegramService {
    override fun incrementScore(userId: String, chatId: String, messageId: String, delta: Int) {
        getScore(userId, chatId).subscribe { getScoreResponse ->
            if (!getScoreResponse.ok) {
                throw GetScoreException(chatId, userId)
            }
            val previousScore = getScoreResponse.result.firstOrNull { it.user.id.toString() == userId }?.score ?: 0
            setScore(
                userId = userId,
                chatId = chatId,
                messageId = messageId,
                newScore = previousScore + delta
            ).subscribe { setScoreResponse ->
                if (!setScoreResponse.ok) {
                    throw SetScoreException(chatId, userId)
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

        }
    }

    private fun getScore(userId: String, chatId: String): Mono<GetScoresResponse> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/getGameHighScores")
                    .queryParam("user_id", userId)
                    .queryParam("inline_message_id", chatId)
                    .build()
            }
            .retrieve()
            .bodyToMono(GetScoresResponse::class.java)
    }

    private fun setScore(userId: String, chatId: String, messageId: String, newScore: Int): Mono<SetScoreResponse> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/setGameScore")
                    .queryParam("user_id", userId)
                    .queryParam("chat_id", chatId)
                    .queryParam("message_id", messageId)
                    .queryParam("score", newScore)
                    .build()
            }
            .retrieve()
            .bodyToMono(SetScoreResponse::class.java)
    }
}