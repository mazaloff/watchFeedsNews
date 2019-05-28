package ru.observe.twits.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import ru.observe.twits.uimodels.TypeChannel
import java.lang.Exception

class SharedHelper(context: Context) {

    companion object{
        private const val NAME_SHARED = "HELPER"
        private const val KEY_TYPE_CHANNEL = "KEY_TYPE_CHANNEL"
    }

    private val mSharedPreferences: SharedPreferences
    private val mGson: Gson

    init {
        mSharedPreferences =  context.getSharedPreferences(NAME_SHARED, Context.MODE_PRIVATE)
        mGson = Gson()
    }

    fun getTypeChannel() : TypeChannel {
        return try {
            mGson.fromJson(mSharedPreferences.getString(KEY_TYPE_CHANNEL, ""), TypeChannel::class.java)
        }catch (e: Exception) {
            TypeChannel.BBC
        }
    }

    fun setTypeChannel(value: TypeChannel) {
        mSharedPreferences.edit().putString(KEY_TYPE_CHANNEL, mGson.toJson(value, TypeChannel::class.java)).apply()
    }

}