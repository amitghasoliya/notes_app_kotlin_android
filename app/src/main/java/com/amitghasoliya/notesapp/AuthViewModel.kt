package com.amitghasoliya.notesapp

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitghasoliya.notesapp.models.UserDelete
import com.amitghasoliya.notesapp.models.UserRequest
import com.amitghasoliya.notesapp.models.UserResponse
import com.amitghasoliya.notesapp.repository.UserRepository
import com.amitghasoliya.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    val userResponseStateFlow: StateFlow<NetworkResult<UserResponse>>
        get() = userRepository.userResponseStateFlow

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun deleteUser(id:UserDelete){
        viewModelScope.launch {
            userRepository.deleteUser(id)
        }
    }

    fun validateCredential(email:String, password:String, username:String, isLogin: Boolean): Pair<Boolean, String>{
        var result = Pair(true, "")
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username) && !isLogin){
            return Pair(false, "Please fill details")
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false, "Email is invalid")
        }
        else if(password.length <= 5){
            result = Pair(false, "Password length should be greater than 5")
        }
        return result
    }
}