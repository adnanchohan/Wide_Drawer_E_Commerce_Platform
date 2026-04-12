package com.drawer.platform.data.db

import androidx.room.*
import com.drawer.platform.data.model.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE buyerId = :buyerId ORDER BY createdAt DESC")
    fun getOrdersByBuyer(buyerId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getOrdersBySeller(sellerId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE deliveryPersonId = :deliveryPersonId ORDER BY createdAt DESC")
    fun getOrdersByDeliveryPerson(deliveryPersonId: Long): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE status = 'PENDING' AND deliveryPersonId IS NULL ORDER BY createdAt DESC")
    fun getPendingOrders(): Flow<List<OrderEntity>>

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, status: String)

    @Query("UPDATE orders SET deliveryPersonId = :deliveryPersonId, status = 'ACCEPTED' WHERE id = :orderId")
    suspend fun acceptOrder(orderId: Long, deliveryPersonId: Long)
}
