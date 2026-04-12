package com.drawer.platform.auth.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.UserEntity
import com.drawer.platform.data.repository.UserRepository
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val userId: Long) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UserRepository(AppDatabase.getInstance(app).userDao())

    private val _state = MutableLiveData<RegisterState>(RegisterState.Idle)
    val state: LiveData<RegisterState> = _state

    fun register(name: String, email: String, password: String, phone: String, mode: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || phone.isBlank()) {
            _state.value = RegisterState.Error("All fields are required"); return
        }
        if (password.length < 6) {
            _state.value = RegisterState.Error("Password must be at least 6 characters"); return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.value = RegisterState.Error("Enter a valid email address"); return
        }
        _state.value = RegisterState.Loading
        viewModelScope.launch {
            if (repo.emailExists(email)) {
                _state.value = RegisterState.Error("Email already registered"); return@launch
            }
            val user = UserEntity(
                name = name.trim(), email = email.trim(),
                passwordHash = repo.hashPassword(password),
                phone = phone.trim(), mode = mode
            )
            val id = repo.registerUser(user)
            _state.value = RegisterState.Success(id)
        }
    }
}
