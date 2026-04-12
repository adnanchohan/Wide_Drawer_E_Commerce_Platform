package com.drawer.platform.buyer.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.data.repository.ProductRepository

class SearchViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ProductRepository(AppDatabase.getInstance(app).productDao())

    private val _query = MutableLiveData("")
    val query: LiveData<String> = _query

    val results: LiveData<List<ProductEntity>> = _query.switchMap { q ->
        if (q.isBlank()) repo.getAllProducts().asLiveData()
        else repo.searchProducts(q).asLiveData()
    }

    fun search(query: String) { _query.value = query }
}
