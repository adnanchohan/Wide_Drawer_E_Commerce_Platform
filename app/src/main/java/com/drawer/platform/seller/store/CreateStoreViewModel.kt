package com.drawer.platform.seller.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.StoreEntity
import com.drawer.platform.data.repository.StoreRepository
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class CreateStoreViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = StoreRepository(AppDatabase.getInstance(app).storeDao())
    private val prefs = SharedPrefManager.getInstance(app)

    private val _existing = MutableLiveData<StoreEntity?>()
    val existing: LiveData<StoreEntity?> = _existing

    private val _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean> = _saved

    init { viewModelScope.launch { _existing.value = repo.getStoreBySellerId(prefs.getUserId()) } }

    fun saveStore(name: String, desc: String, phone: String, address: String, bannerPath: String?) {
        viewModelScope.launch {
            val current = _existing.value
            if (current != null) {
                repo.updateStore(current.copy(
                    storeName = name, description = desc, phone = phone, address = address,
                    bannerImagePath = bannerPath ?: current.bannerImagePath
                ))
            } else {
                repo.createStore(StoreEntity(
                    sellerId = prefs.getUserId(), storeName = name,
                    description = desc, phone = phone, address = address, bannerImagePath = bannerPath
                ))
            }
            _saved.value = true
        }
    }
}
