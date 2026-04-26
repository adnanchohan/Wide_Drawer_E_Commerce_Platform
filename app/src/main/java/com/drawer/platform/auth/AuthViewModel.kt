package com.drawer.platform.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.UserEntity
import com.drawer.platform.data.repository.UserRepository
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UserRepository(AppDatabase.getInstance(app).userDao())
    private val prefs = SharedPrefManager.getInstance(app)

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = repo.login(email, pass)
            if (user != null) {
                saveSessionAndSuccess(user)
            } else {
                _authState.value = AuthState.Error("Invalid credentials")
            }
        }
    }

    fun register(name: String, email: String, pass: String, mode: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (repo.emailExists(email)) {
                _authState.value = AuthState.Error("Email already registered")
                return@launch
            }
            val user = UserEntity(
                name = name.trim(),
                email = email.trim(),
                passwordHash = repo.hashPassword(pass),
                phone = "",
                mode = mode
            )
            val id = repo.registerUser(user)
            val createdUser = repo.getUserById(id)
            if (createdUser != null) {
                saveSessionAndSuccess(createdUser)
            } else {
                _authState.value = AuthState.Error("Failed to register")
            }
        }
    }

    private fun saveSessionAndSuccess(user: UserEntity) {
        prefs.saveUserSession(user.id, user.mode, user.name, user.email)
        _authState.value = AuthState.Success
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val msg: String) : AuthState()
}
