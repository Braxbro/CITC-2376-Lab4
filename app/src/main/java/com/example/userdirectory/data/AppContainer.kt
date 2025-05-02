package com.example.userdirectory.data

import android.app.Application
import com.example.userdirectory.network.UserProfileApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// To be honest, I'm not entirely sure what the point of all of this is.
// It looks like a really roundabout way of doing things.
// But it means I don't have to jump between files as much, so I won't complain.
// Much.

// If I wasn't running beyond my usual limits and approaching burnout at mach suffering
// I'd try to do this with less roundabout nonsense.
// But I have an example that works, so I'm going to replicate that
// and tweak it to fit specifications.

interface AppContainer {
    val userRepository: UserRepository
}

class DefaultAppContainer : AppContainer {
    private val apiUrl = "https://randomuser.me/api/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(apiUrl)
        .build()

    private val retrofitService: UserProfileApiService by lazy {
        retrofit.create(UserProfileApiService::class.java)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(retrofitService)
    }
}

class UserDirApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}