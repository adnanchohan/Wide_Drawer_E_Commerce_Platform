package com.drawer.platform.seller.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.OrderEntity
import com.drawer.platform.data.repository.OrderRepository
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class SellerOrdersViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = OrderRepository(AppDatabase.getInstance(app).orderDao())
    private val sellerId = SharedPrefManager.getInstance(app).getUserId()

    val orders: LiveData<List<OrderEntity>> = repo.getOrdersBySeller(sellerId).asLiveData()

    fun updateStatus(orderId: Long, status: String) {
        viewModelScope.launch { repo.updateOrderStatus(orderId, status) }
    }
}
