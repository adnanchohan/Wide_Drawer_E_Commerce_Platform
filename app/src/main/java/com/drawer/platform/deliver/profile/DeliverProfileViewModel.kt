package com.drawer.platform.deliver.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.UserEntity
import com.drawer.platform.data.repository.UserRepository
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class DeliverProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UserRepository(AppDatabase.getInstance(app).userDao())
    private val prefs = SharedPrefManager.getInstance(app)

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> = _user

    init { viewModelScope.launch { _user.value = repo.getUserById(prefs.getUserId()) } }

    fun logout(onDone: () -> Unit) { prefs.clearSession(); onDone() }
}
