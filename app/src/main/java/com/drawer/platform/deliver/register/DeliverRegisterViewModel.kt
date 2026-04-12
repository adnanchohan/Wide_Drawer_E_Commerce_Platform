package com.drawer.platform.deliver.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.repository.UserRepository
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class DeliverRegisterViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UserRepository(AppDatabase.getInstance(app).userDao())
    private val prefs = SharedPrefManager.getInstance(app)

    private val _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean> = _saved

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun saveProfile(vehicleType: String, coverageArea: String, idProofPath: String?) {
        if (vehicleType.isBlank()) { _error.value = "Please enter your vehicle type"; return }
        viewModelScope.launch {
            val user = repo.getUserById(prefs.getUserId()) ?: return@launch
            repo.updateUser(user.copy(
                vehicleType = vehicleType,
                address = coverageArea,
                idProofPath = idProofPath
            ))
            _saved.value = true
        }
    }
}
