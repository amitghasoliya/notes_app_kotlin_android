package com.amitghasoliya.notesapp.api

import com.amitghasoliya.notesapp.models.UserDelete
import com.amitghasoliya.notesapp.models.UserRequest
import com.amitghasoliya.notesapp.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

interface UserAPI {
    @POST("/users/signup")
    suspend fun signup(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("/users/signin")
    suspend fun signin(@Body userRequest: UserRequest): Response<UserResponse>

    @HTTP(method = "DELETE", path = "/users/delete", hasBody = true)
    suspend fun deleteAccount(@Body id: UserDelete): Response<UserResponse>

}