package com.drawer.platform.data.repository

import com.drawer.platform.data.db.OrderDao
import com.drawer.platform.data.model.OrderEntity
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderDao: OrderDao) {

    suspend fun placeOrder(order: OrderEntity): Long = orderDao.insertOrder(order)
    suspend fun updateOrder(order: OrderEntity) = orderDao.updateOrder(order)
    suspend fun updateOrderStatus(orderId: Long, status: String) = orderDao.updateOrderStatus(orderId, status)
    suspend fun acceptOrder(orderId: Long, deliveryPersonId: Long) = orderDao.acceptOrder(orderId, deliveryPersonId)

    fun getOrdersByBuyer(buyerId: Long): Flow<List<OrderEntity>> = orderDao.getOrdersByBuyer(buyerId)
    fun getOrdersBySeller(sellerId: Long): Flow<List<OrderEntity>> = orderDao.getOrdersBySeller(sellerId)
    fun getPendingOrders(): Flow<List<OrderEntity>> = orderDao.getPendingOrders()
    fun getOrdersByDeliveryPerson(id: Long): Flow<List<OrderEntity>> = orderDao.getOrdersByDeliveryPerson(id)
}
