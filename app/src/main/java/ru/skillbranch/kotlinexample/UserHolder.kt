package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        val user = User.makeUser(fullName, email = email, password = password)
        return when {
            map.filter { it.key.trim() == user.login.trim() }.isEmpty() -> {
                map[user.login] = user
                user
            }
            else -> throw IllegalArgumentException("A user with this email already exists")
        }
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        val user = User.makeUser(fullName, phone = rawPhone)
        return when {
            !isValidPhone(user.login) -> throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
            map.filter { it.key == rawPhone }.isEmpty() -> {
                map[rawPhone] = user
                user
            }
            else -> throw IllegalArgumentException("A user with this phone already exists")
        }
    }

    fun loginUser(login: String, password: String): String? {
        return map[login.trim()]?.let {
            if (it.checkPassword(password)) it.userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) = map[login.trim()]?.changeAccessCode()

    private fun isValidPhone(phone: String) = phone.matches(Regex("""\+[\d]{11}"""))

    fun importUsers(list: List<String>): List<User> {
        val result: MutableList<User> = mutableListOf()
            list.forEach {
                val parts = it.split(";")
                val fullName = parts[0].trim()
                var email: String? = parts[1].trim()
                val (salt, passwordHash) = parts[2].trim().split(":")
                var phone: String? = parts[3]
                if (email.isNullOrBlank()) email = null
                if (phone.isNullOrBlank()) phone = null
                val importMeta = mapOf("src" to "csv")
                val user = User.makeUser(
                    fullName,
                    email = email,
                    phone = phone,
                    meta = importMeta,
                    passwordMeta = salt to passwordHash
                )
                map[user.login] = user
                result.add(user)
            }
            return result
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}