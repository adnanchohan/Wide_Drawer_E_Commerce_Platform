package com.drawer.platform.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val buyerId: Long,
    val productId: Long,
    val productName: String,
    val productPrice: Double,
    val productImagePath: String = "",
    val storeName: String = "",
    val sellerId: Long = 0,
    val storeId: Long = 0,
    val quantity: Int = 1,
    val addedAt: Long = System.currentTimeMillis()
)
