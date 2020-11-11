package com.example.threelines.Data

import android.content.Context
import android.content.SharedPreferences

class LoginPreferences(context: Context) {

    val PREFS_FILENAME = "login"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var user_id: String?
        get() = prefs.getString("user_id", "")
        set(value) = prefs.edit().putString("user_id", value).apply()

    var passwd: String?
        get() = prefs.getString("passwd", "")
        set(value) = prefs.edit().putString("passwd", value).apply()
}