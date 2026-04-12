package com.drawer.platform.buyer.reels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.data.repository.ProductRepository

class ReelsViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ProductRepository(AppDatabase.getInstance(app).productDao())
    val reelProducts: LiveData<List<ProductEntity>> = repo.getProductsWithVideos().asLiveData()
}
