package com.drawer.platform.data.repository

import com.drawer.platform.data.db.UserDao
import com.drawer.platform.data.model.UserEntity
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: UserEntity): Long = userDao.insertUser(user)

    suspend fun login(email: String, password: String): UserEntity? =
        userDao.login(email.trim(), hashPassword(password))

    suspend fun getUserById(id: Long): UserEntity? = userDao.getUserById(id)

    suspend fun emailExists(email: String): Boolean = userDao.emailExists(email.trim()) > 0

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(password.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }
}
