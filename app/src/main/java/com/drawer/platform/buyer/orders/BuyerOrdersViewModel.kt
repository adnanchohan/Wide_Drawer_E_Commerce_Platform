package com.drawer.platform.buyer.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.OrderEntity
import com.drawer.platform.data.repository.OrderRepository
import com.drawer.platform.utils.SharedPrefManager

class BuyerOrdersViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = OrderRepository(AppDatabase.getInstance(app).orderDao())
    private val buyerId = SharedPrefManager.getInstance(app).getUserId()
    val orders: LiveData<List<OrderEntity>> = repo.getOrdersByBuyer(buyerId).asLiveData()
}
