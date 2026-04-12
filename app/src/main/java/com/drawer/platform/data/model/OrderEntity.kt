package com.drawer.platform.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val buyerId: Long,
    val sellerId: Long,
    val deliveryPersonId: Long? = null,
    val productId: Long,
    val productName: String,
    val productPrice: Double,
    val quantity: Int = 1,
    val status: String = "PENDING", // PENDING | ACCEPTED | PICKED_UP | DELIVERED | CANCELLED
    val buyerAddress: String,
    val sellerAddress: String,
    val buyerPhone: String = "",
    val sellerPhone: String = "",
    val buyerName: String = "",
    val sellerName: String = "",
    val storeName: String = "",
    val productImagePath: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
