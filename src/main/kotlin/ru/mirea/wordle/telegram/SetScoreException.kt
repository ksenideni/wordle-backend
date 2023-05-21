package ru.mirea.wordle.telegram

class SetScoreException(chatId: String, messageId: Int, userId: Int):
        RuntimeException("Could not set score for user with id $userId in chat $chatId with message $messageId")