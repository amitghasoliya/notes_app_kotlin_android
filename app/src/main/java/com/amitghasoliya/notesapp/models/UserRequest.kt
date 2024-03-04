package com.amitghasoliya.notesapp.models

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)
data class UserDelete(
    val id: String
)