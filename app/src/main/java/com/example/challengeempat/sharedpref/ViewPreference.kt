package com.example.challengeempat.sharedpref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class ViewPreference(context: Context) {
    companion object {
        private const val PREFERENCE_NAME = "ViewPreference"
        private const val KEY_LAYOUT_PREFERENCE = "layout_preference"
    }
    private val preference: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    fun saveLayoutPref(isView: Boolean) {
        preference.edit().putBoolean(KEY_LAYOUT_PREFERENCE, isView).apply()
        Log.d("ViewPreference", "Layout preference saved: isView=$isView")
    }
    fun getLayoutPref(): Boolean {
        val layoutPref = preference.getBoolean(KEY_LAYOUT_PREFERENCE, true)
        Log.d("ViewPreference", "Layout preference retrieved: isView=$layoutPref")
        return layoutPref
    }
}


