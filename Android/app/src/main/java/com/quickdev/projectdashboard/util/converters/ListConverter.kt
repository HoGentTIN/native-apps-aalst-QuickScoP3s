package com.quickdev.projectdashboard.util.converters

import androidx.room.TypeConverter
import com.quickdev.projectdashboard.models.domain.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class IntListConverter {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toIntList(value: String): List<Int>? {
        val intListType = Types.newParameterizedType(MutableList::class.java, Int::class.javaObjectType)
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(intListType)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun toIntListJson(list: List<Int>): String? {
        val intListType = Types.newParameterizedType(MutableList::class.java, Int::class.javaObjectType)
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(intListType)
        return adapter.toJson(list)
    }
}

class UserListConverter {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun toUserList(value: String): List<User>? {
        val intListType = Types.newParameterizedType(MutableList::class.java, User::class.java)
        val adapter: JsonAdapter<List<User>> = moshi.adapter(intListType)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun toUserListJson(list: List<User>): String? {
        val intListType = Types.newParameterizedType(MutableList::class.java, User::class.java)
        val adapter: JsonAdapter<List<User>> = moshi.adapter(intListType)
        return adapter.toJson(list)
    }
}
