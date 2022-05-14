package com.softic.esraa.elnajjar.utils

import android.content.Context

class PreferencesUtility {

    companion object {

        private const val PREFERENCES_FILE_NAME = "prefs"



        fun setString(context: Context, key: String, value: String) {

            context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).edit().apply {
                putString(key, value)
                apply()
            }

        }

        fun getString(context: Context, key: String, defaultValue: String = ""): String {

            return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
                .getString(key, defaultValue)!!
        }


        fun setInt(context: Context, key: String, value: Int) {

            context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).edit().apply {
                putInt(key, value)
                apply()
            }

        }

        fun getInt(context: Context, key: String, defaultValue: Int = -1): Int {

            return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
                .getInt(key, defaultValue)
        }


        fun setBoolean(context: Context, key: String, value: Boolean) {

            context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).edit().apply {
                putBoolean(key, value)
                apply()
            }

        }

        fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {

            return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
                .getBoolean(key, defaultValue)
        }


        fun remove(context: Context, key: String){

            context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).edit().apply {
                remove(key)
                apply()
            }

        }

        fun clearAllData(context: Context) {

            context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE).edit().apply {
                clear()
                apply()
            }

        }
    }
}