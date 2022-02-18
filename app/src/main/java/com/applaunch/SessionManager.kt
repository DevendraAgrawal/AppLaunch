package com.applaunch

import android.content.Context
import android.content.SharedPreferences

class SessionManager {

    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var context: Context
    var PRIVATE_MODE: Int = 0

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {
        const val PREF_NAME: String = "SessionDemo"
        const val IS_LOGIN: String = "isLoggedIn"
    }

    fun getIsLogin(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun setIsAppOpen(isLogin: Boolean) {
        editor.putBoolean(IS_LOGIN, isLogin).commit()
    }
}