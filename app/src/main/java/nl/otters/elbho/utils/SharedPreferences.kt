package nl.otters.elbho.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

//http://www.kotlincodes.com/kotlin/shared-preferences-with-kotlin/
class SharedPreferences(context: Context) {
    private val PREFS_NAME = "ELHBO"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    fun save(KEY_NAME: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, value)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun clear() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}