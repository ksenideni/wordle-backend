package ru.mirea.wordle.telegram

class GetScoreException(chatId: String, messageId: Int, userId: Int):
        RuntimeException("Could not get score for user with id $userId in chat $chatId with message $messageId")