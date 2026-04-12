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

class AvailableOrdersViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = OrderRepository(AppDatabase.getInstance(app).orderDao())
    private val deliverId = SharedPrefManager.getInstance(app).getUserId()

    val availableOrders: LiveData<List<OrderEntity>> = repo.getPendingOrders().asLiveData()

    fun acceptOrder(orderId: Long) {
        viewModelScope.launch { repo.acceptOrder(orderId, deliverId) }
    }
}
