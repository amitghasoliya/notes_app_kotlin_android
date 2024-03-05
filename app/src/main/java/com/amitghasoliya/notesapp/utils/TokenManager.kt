package com.amitghasoliya.notesapp.utils

import android.content.Context
import com.amitghasoliya.notesapp.utils.Constants.PREFS_TOKEN_FILE
import com.amitghasoliya.notesapp.utils.Constants.USER_EMAIL
import com.amitghasoliya.notesapp.utils.Constants.USER_ID
import com.amitghasoliya.notesapp.utils.Constants.USER_NAME
import com.amitghasoliya.notesapp.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token:String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }
    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
    fun saveUserId(token:String){
        val editor = prefs.edit()
        editor.putString(USER_ID, token)
        editor.apply()
    }
    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }
    fun saveUsername(token:String){
        val editor = prefs.edit()
        editor.putString(USER_NAME, token)
        editor.apply()
    }
    fun getUsername(): String? {
        return prefs.getString(USER_NAME, null)
    }
    fun saveEmail(token:String){
        val editor = prefs.edit()
        editor.putString(USER_EMAIL, token)
        editor.apply()
    }
    fun getEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }
    fun logOut(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
        editor.commit()
    }
}