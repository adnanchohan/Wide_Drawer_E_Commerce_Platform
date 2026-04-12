package com.drawer.platform.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val storeId: Long,
    val sellerId: Long,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imagePaths: String = "",   // comma-separated file paths
    val videoPath: String? = null,
    val stock: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
