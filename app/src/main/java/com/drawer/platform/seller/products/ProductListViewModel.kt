package com.drawer.platform.seller.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.data.repository.ProductRepository
import com.drawer.platform.utils.SharedPrefManager

class ProductListViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ProductRepository(AppDatabase.getInstance(app).productDao())
    val sellerId: Long = SharedPrefManager.getInstance(app).getUserId()
    val products: LiveData<List<ProductEntity>> = repo.getProductsBySeller(sellerId).asLiveData()
}
