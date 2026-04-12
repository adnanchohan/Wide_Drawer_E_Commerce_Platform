package com.drawer.platform.buyer.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.data.repository.ProductRepository

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ProductRepository(AppDatabase.getInstance(app).productDao())

    val allProducts: LiveData<List<ProductEntity>> = repo.getAllProducts().asLiveData()

    private val _selectedCategory = MutableLiveData("All")
    val selectedCategory: LiveData<String> = _selectedCategory

    val filteredProducts: LiveData<List<ProductEntity>> = _selectedCategory.switchMap { cat ->
        if (cat == "All") repo.getAllProducts().asLiveData()
        else repo.getProductsByCategory(cat).asLiveData()
    }

    fun selectCategory(category: String) { _selectedCategory.value = category }
}
