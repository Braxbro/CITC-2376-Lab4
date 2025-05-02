package com.example.userdirectory.data

import com.example.userdirectory.data.types.UserProfileResults
import com.example.userdirectory.network.UserProfileApiService

// Trying the approach given in the MarsPhotos example.
// Will probably need to adapt for different JSON parser.

interface UserRepository {
    suspend fun getUsers(): UserProfileResults
}

// Network ver of UserRepository
class NetworkUserRepository(
    private val userProfileApiService: UserProfileApiService
) : UserRepository {
    override suspend fun getUsers(): UserProfileResults =
        userProfileApiService.getUsers()
}