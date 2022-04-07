package com.example.buyonmars.base

import android.content.Context
import android.content.SharedPreferences

open class AndroidServiceManager(val context: Context) : ServiceManager {

    companion object {
        var MARS_PREFS: String = "mars_preferences"
        var SLIDE_VIEW_SHOWN = "slide_shown"
    }

    fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(MARS_PREFS, Context.MODE_PRIVATE)
    }

    fun getEditor(): SharedPreferences.Editor {
        var sharedPreferences = context.getSharedPreferences(MARS_PREFS, Context.MODE_PRIVATE)
        return sharedPreferences.edit()
    }

    override fun slideWasShown(): Boolean {
        return getSharedPreferences().getBoolean(SLIDE_VIEW_SHOWN, false)
    }

    override fun setSlideWasShown(wasShown: Boolean) {
        var editor = getEditor()

        editor.putBoolean(SLIDE_VIEW_SHOWN, wasShown)

        editor.apply()
    }
}