package com.example.challengeempat.sharedpref

import android.content.Context
import android.content.SharedPreferences

class SharedPreffUser(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val isLoggedInKey = "isLoggedIn"

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(isLoggedInKey, false)
    }

    fun setLoggedIn(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(isLoggedInKey, value)
        editor.apply()
    }
}

