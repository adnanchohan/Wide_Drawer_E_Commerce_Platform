package com.drawer.platform.auth.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.UserEntity
import com.drawer.platform.data.repository.UserRepository
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: UserEntity) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UserRepository(AppDatabase.getInstance(app).userDao())

    private val _state = MutableLiveData<LoginState>(LoginState.Idle)
    val state: LiveData<LoginState> = _state

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = LoginState.Error("Please fill in all fields"); return
        }
        _state.value = LoginState.Loading
        viewModelScope.launch {
            val user = repo.login(email, password)
            _state.value = if (user != null) LoginState.Success(user)
            else LoginState.Error("Invalid email or password")
        }
    }
}
