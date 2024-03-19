package com.amitghasoliya.notesapp.repository

import com.amitghasoliya.notesapp.api.UserAPI
import com.amitghasoliya.notesapp.models.UserDelete
import com.amitghasoliya.notesapp.models.UserRequest
import com.amitghasoliya.notesapp.models.UserResponse
import com.amitghasoliya.notesapp.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI){

    private val _userResponseStateFlow = MutableStateFlow<NetworkResult<UserResponse>>(NetworkResult.Loading())
    val userResponseStateFlow: StateFlow<NetworkResult<UserResponse>>
        get() = _userResponseStateFlow

    suspend fun registerUser(userReq: UserRequest){
        val response = userAPI.signup(userReq)
        handleResponse(response)
    }

    suspend fun loginUser(userReq: UserRequest){
        val response = userAPI.signin(userReq)
        handleResponse(response)
    }

    suspend fun deleteUser(id: UserDelete){
        userAPI.deleteAccount(id)
    }

    private suspend fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseStateFlow.emit(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userResponseStateFlow.emit(NetworkResult.Error(errorObj.getString("message")))
            }catch (e:Exception){
                _userResponseStateFlow.emit(NetworkResult.Error("Something Went Wrong"))
            }
        }
        else{
            _userResponseStateFlow.emit(NetworkResult.Error("Something Went Wrong"))
        }
    }
}