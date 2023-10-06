package com.example.challengeempat.sharedpref

import android.content.Context
import android.content.SharedPreferences

class ViewPreference (contex: Context){

    companion object{
        private const val LOCK = "ViewPreference"

    }
    private val preference : SharedPreferences =contex.getSharedPreferences(LOCK,Context.MODE_PRIVATE)

    fun saveLayoutPref(isView: Boolean) {
        preference.edit().putBoolean(LOCK,isView).apply()
    }
    fun getLayoutPref(): Boolean{
        return preference.getBoolean(LOCK,true)

    }

}