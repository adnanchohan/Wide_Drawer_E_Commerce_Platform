package com.drawer.platform.seller.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.StoreEntity
import com.drawer.platform.data.repository.StoreRepository
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class StoreViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = StoreRepository(AppDatabase.getInstance(app).storeDao())
    val sellerId: Long = SharedPrefManager.getInstance(app).getUserId()

    val store: LiveData<StoreEntity?> = repo.observeStoreBySellerId(sellerId).asLiveData()
}
