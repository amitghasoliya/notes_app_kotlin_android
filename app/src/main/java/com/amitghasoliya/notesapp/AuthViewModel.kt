package com.amitghasoliya.notesapp

import android.text.TextUtils
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitghasoliya.notesapp.models.UserDelete
import com.amitghasoliya.notesapp.models.UserRequest
import com.amitghasoliya.notesapp.models.UserResponse
import com.amitghasoliya.notesapp.repository.UserRepository
import com.amitghasoliya.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<UserResponse?>(null)
    val user: StateFlow<UserResponse?> = _user

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    init {
        observeAuth()
    }

    private fun observeAuth() {
        viewModelScope.launch {
            userRepository.userResponseStateFlow.collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _user.emit(it.data)
                    }
                    is NetworkResult.Error -> {
                        _errorMessage.value = it.message
                    }
                    is NetworkResult.Loading -> {
                    }
                }
            }
        }
    }

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