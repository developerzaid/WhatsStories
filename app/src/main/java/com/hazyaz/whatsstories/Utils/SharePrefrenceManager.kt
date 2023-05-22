package com.hazyaz.whatsstories.Utils

import android.content.Context
import android.content.SharedPreferences

class SharePreferencesManager constructor(context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "whatStories"
        private const val STATUS_FOLDER = "statusFolder"
        private const val BS_STATUS_FOLDER = "bsStatusFolder"
    }

    private val sharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private inline fun SharedPreferences.put(body: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        editor.body()
        editor.apply()
    }

    var statusFolder: String?
        get() = sharedPreferences.getString(STATUS_FOLDER, "")
        set(value) = sharedPreferences.put { putString(STATUS_FOLDER, value) }

    var bsStatusFolder: String?
        get() = sharedPreferences.getString(BS_STATUS_FOLDER, null)
        set(value) = sharedPreferences.put { putString(BS_STATUS_FOLDER, value) }

}

