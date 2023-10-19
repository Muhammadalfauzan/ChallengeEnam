package com.example.challengeempat

import android.content.Context
import android.content.SharedPreferences


class SharedPreffUser(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("loggedIn", false)
    }

    fun setLoggedIn(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("loggedIn", value)
        editor.apply()
    }

    fun saveUserData(username: String, noTelepon: String, email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("noTelepon", noTelepon)
        editor.putString("email", email)
        editor.apply()
    }

    fun getUsername(): String {
        return sharedPreferences.getString("username", "") ?: ""
    }

    fun getNoTelepon(): String {
        return sharedPreferences.getString("noTelepon", "") ?: ""
    }

    fun getEmail(): String {
        return sharedPreferences.getString("email", "") ?: ""
    }

    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.remove("noTelepon")
        editor.remove("email")
        editor.apply()
    }
}
