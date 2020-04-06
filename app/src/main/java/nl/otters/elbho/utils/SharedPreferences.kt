package nl.otters.elbho.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

const val PREFS_NAME = "ELHBO"

class SharedPreferences(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    fun save(KEY_NAME: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, value)
        editor.apply()
    }

    fun getValueBoolean(KEY_NAME: String): Boolean? {
        return sharedPref.getBoolean(KEY_NAME, false)
    }
    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun clear() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun clear(keyName: String) {
        sharedPref.edit().remove(keyName).apply()
    }

    fun setArrayPrefs(
        arrayName: String,
        array: ArrayList<String?>
    ) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(arrayName + "_size", array.size)
        for (i in 0 until array.size) editor.putString(arrayName + "_" + i, array[i])
        editor.apply()
    }

    fun getArrayPrefs(
        arrayName: String
    ): ArrayList<String>? {
        val size: Int = sharedPref.getInt(arrayName + "_size", 0)
        val array: ArrayList<String> = ArrayList(size)
        for (i in 0 until size) array.add(sharedPref.getString(arrayName + "_" + i, null)!!)
        return array
    }

}