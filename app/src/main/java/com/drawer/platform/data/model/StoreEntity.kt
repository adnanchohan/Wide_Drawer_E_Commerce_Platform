package com.drawer.platform.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sellerId: Long,
    val storeName: String,
    val description: String,
    val bannerImagePath: String? = null,
    val address: String = "",
    val phone: String = "",
    val rating: Float = 0f,
    val createdAt: Long = System.currentTimeMillis()
)
