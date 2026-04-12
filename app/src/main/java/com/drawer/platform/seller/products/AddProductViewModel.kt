package com.drawer.platform.seller.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.data.repository.ProductRepository
import com.drawer.platform.data.repository.StoreRepository
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class AddProductViewModel(app: Application) : AndroidViewModel(app) {
    private val productRepo = ProductRepository(AppDatabase.getInstance(app).productDao())
    private val storeRepo = StoreRepository(AppDatabase.getInstance(app).storeDao())
    private val prefs = SharedPrefManager.getInstance(app)

    private val _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean> = _saved

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun saveProduct(
        name: String, description: String, price: String, category: String,
        stock: String, imagePaths: List<String>, videoPath: String?
    ) {
        if (name.isBlank() || price.isBlank()) {
            _error.value = "Name and price are required"; return
        }
        val priceDouble = price.toDoubleOrNull()
        if (priceDouble == null || priceDouble <= 0) {
            _error.value = "Enter a valid price"; return
        }
        viewModelScope.launch {
            val store = storeRepo.getStoreBySellerId(prefs.getUserId())
            if (store == null) {
                _error.value = "Please create your store first"; return@launch
            }
            productRepo.addProduct(
                ProductEntity(
                    storeId = store.id,
                    sellerId = prefs.getUserId(),
                    name = name.trim(),
                    description = description.trim(),
                    price = priceDouble,
                    category = category,
                    imagePaths = imagePaths.joinToString(","),
                    videoPath = videoPath,
                    stock = stock.toIntOrNull() ?: 0
                )
            )
            _saved.value = true
        }
    }
}
