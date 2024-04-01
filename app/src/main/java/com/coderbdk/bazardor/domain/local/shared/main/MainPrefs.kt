package com.coderbdk.bazardor.domain.local.shared.main

import android.content.Context
import android.content.SharedPreferences

class MainPrefs (context: Context){
    private val prefs: SharedPreferences
    init {
        prefs = context.getSharedPreferences("bazardor_main_prefs",Context.MODE_PRIVATE)
    }
    fun putItemPosition(position: Int){
        prefs.edit().putInt("position",position).apply()
    }
    fun getItemPosition(): Int{
        return prefs.getInt("position",0)
    }

}