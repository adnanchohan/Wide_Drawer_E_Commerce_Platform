package com.drawer.platform.deliver.orders

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

class MyDeliveriesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = OrderRepository(AppDatabase.getInstance(app).orderDao())
    private val deliverId = SharedPrefManager.getInstance(app).getUserId()

    val myOrders: LiveData<List<OrderEntity>> = repo.getOrdersByDeliveryPerson(deliverId).asLiveData()

    fun markPickedUp(orderId: Long) {
        viewModelScope.launch { repo.updateOrderStatus(orderId, "PICKED_UP") }
    }

    fun markDelivered(orderId: Long) {
        viewModelScope.launch { repo.updateOrderStatus(orderId, "DELIVERED") }
    }
}
