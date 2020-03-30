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
            map.filter {  it.key.trim() == user.login.trim() }.isEmpty() -> {
                map[user.login] = user
                user
            }
            else -> throw IllegalArgumentException("A user with this email already exists")
        }
    }

    fun registerUserByPhone(fullName: String, rawPhone: String) : User {
        val user = User.makeUser(fullName, phone = rawPhone)
        return when {
            !isValidPhone(user.login) -> throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
            map.filter {  it.key.trim() == rawPhone.trim() }.isEmpty() -> {
                map[rawPhone] = user
                user
            }
            else -> throw IllegalArgumentException("A user with this email already exists")
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

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}