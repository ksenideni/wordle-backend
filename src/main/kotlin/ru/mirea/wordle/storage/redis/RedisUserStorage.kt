package ru.mirea.wordle.storage.redis

import io.lettuce.core.api.StatefulRedisConnection
import org.springframework.stereotype.Repository
import ru.mirea.wordle.game.model.User
import ru.mirea.wordle.storage.UserStorage
import ru.mirea.wordle.storage.redis.exception.UserNotFoundException
import ru.mirea.wordle.storage.redis.model.RedisUserKey
import ru.mirea.wordle.storage.redis.model.UserMapper

/**
 * Класс, необходимый для взаимодейтвия с Redis и работой с информацией о пользователе
 *
 * @param userMapper объект класса для преобразования объекта пользователя в JSON, и обратно
 * @param statefulRedisConnection объект соединения с Redis
 *
 * */
@Repository
class RedisUserStorage(
    val userMapper: UserMapper,
    val userStorageConnection: StatefulRedisConnection<String, String>
) : UserStorage {

    /**
     * Метод, предназначенный для поиска пользователя в хранилище.
     *
     * @param chatId индентификатор чата, в котором находится пользователь
     * @param userId индентификатор пользователя
     *
     * @return объект, хранящий информацию о пользователе
     * @throws UserNotFoundException
     * */
    override fun getUser(chatId: String, userId: String): User {
        val result = userStorageConnection.sync().get(RedisUserKey(chatId, userId).toString())
            ?: throw UserNotFoundException(chatId, userId)
        return userMapper.mapToModel(result)
    }

    /**
     * Метод, предназначенный для обновления информации о пользователе в хранилище
     *
     * @param user объект, хранящий обновленную информацию о пользователе и его прогрессе
     * */
    override fun updateUser(user: User) {
        userStorageConnection.sync().set(
            RedisUserKey(user.chatId, user.userId).toString(),
            userMapper.mapToJson(user)
        )
    }
}