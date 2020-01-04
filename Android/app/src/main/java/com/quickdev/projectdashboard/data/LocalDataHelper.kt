package com.quickdev.projectdashboard.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class LocalDataHelper(dataName: String, context: Context) {

    private val localData: SharedPreferences = context.getSharedPreferences(dataName, MODE_PRIVATE)
    private val dataEditor: SharedPreferences.Editor = localData.edit()

    enum class Key {
        STR_USERPICTURE,
        STR_USERTOKEN,
        STR_USERNAME,
        STR_USERPASS,
        BOOL_ISFIRSTSETUP
    }

    fun put(key: Key, value: String?) {
        dataEditor.putString(key.name, value)
    }

    fun put(key: Key, value: Boolean) {
        dataEditor.putBoolean(key.name, value)
    }

    fun put(key: Key, value: Int) {
        dataEditor.putInt(key.name, value)
    }

    fun put(key: Key, value: Float) {
        dataEditor.putFloat(key.name, value)
    }

    fun put(key: Key, value: Long) {
        dataEditor.putLong(key.name, value)
    }

    fun put(key: Key, value: Set<String>?) {
        dataEditor.putStringSet(key.name, value)
    }


    fun getString(key: Key, defaultValue: String?): String? {
        return localData.getString(key.name, defaultValue)
    }

    fun getString(key: Key): String? {
        return localData.getString(key.name, null)
    }

    fun getInt(key: Key): Int {
        return localData.getInt(key.name, 0)
    }

    fun getInt(key: Key, defaultValue: Int): Int {
        return localData.getInt(key.name, defaultValue)
    }

    fun getLong(key: Key): Long {
        return localData.getLong(key.name, 0)
    }

    fun getLong(key: Key, defaultValue: Long): Long {
        return localData.getLong(key.name, defaultValue)
    }

    fun getFloat(key: Key): Float {
        return localData.getFloat(key.name, 0f)
    }

    fun getFloat(key: Key, defaultValue: Float): Float {
        return localData.getFloat(key.name, defaultValue)
    }

    fun getBoolean(key: Key, defaultValue: Boolean): Boolean {
        return localData.getBoolean(key.name, defaultValue)
    }

    fun getBoolean(key: Key): Boolean {
        return localData.getBoolean(key.name, false)
    }

    fun remove(key: Key) {
        dataEditor.remove(key.name)
    }

    fun applyChanges() {
        dataEditor.apply()
    }
}
