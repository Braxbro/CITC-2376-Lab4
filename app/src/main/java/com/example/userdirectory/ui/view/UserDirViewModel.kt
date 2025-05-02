package com.example.userdirectory.ui.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.userdirectory.data.UserDirApplication
import com.example.userdirectory.data.UserRepository
import com.example.userdirectory.data.types.UserProfile
import com.example.userdirectory.data.types.UserProfileResults
import kotlinx.coroutines.launch

// I had to single this one out. The IDE gave this to me as an automatic import option.
// This is even more cursed than the interface it involves.
// And yet it's unironically an awesome shorthand assistant
// I hate you Kotlin, but I gotta admit: you've got some funny tricks
import com.example.userdirectory.ui.view.UserDirUiState.*
import retrofit2.HttpException
import java.io.IOException

// Funny state thing. Honestly kind of cuter than doing an enum value lmao
// Thanks example project for the idea to do this.
// I didn't even know you could have interfaces include things that extend themselves.
// That's... cursed as hell. I love it
sealed interface UserDirUiState {
    data class Success(val users: UserProfileResults) : UserDirUiState
    object Error : UserDirUiState
    object Loading : UserDirUiState
}

class UserDirViewModel(private val userRepository: UserRepository) : ViewModel() {
    var uiState: UserDirUiState by mutableStateOf(Loading)
        private set

    // Fetch user data on init
    init {
        getUserData()
    }

    fun getUserData() {
        viewModelScope.launch {
            uiState = Loading
            // we love raw try/catches
            uiState = try {
                // USE THE CURSED IMPORT HAHAHAHA
                Success(userRepository.getUsers())
            }
            catch (e: IOException) {
                Error
            }
            catch (e: HttpException) {
                Error
            }
        }
    }

    // I'm usually not a huge fan of builders and factories.
    // That said, I forget if you can make a viewmodel with an argument without it.
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as UserDirApplication)
                val userRepository = application.container.userRepository
                UserDirViewModel(userRepository)
            }
        }
    }
}