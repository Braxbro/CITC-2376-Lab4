package com.example.userdirectory.network

import com.example.userdirectory.data.types.UserProfileResults
import retrofit2.http.GET

interface UserProfileApiService {
    @GET("?results=20")
    suspend fun getUsers(): UserProfileResults
}