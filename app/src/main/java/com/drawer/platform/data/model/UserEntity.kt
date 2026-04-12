package com.drawer.platform.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val phone: String,
    val mode: String, // SELLER | BUYER | DELIVER
    val profileImagePath: String? = null,
    val address: String = "",
    val vehicleType: String = "", // for DELIVER mode
    val idProofPath: String? = null, // for DELIVER mode
    val createdAt: Long = System.currentTimeMillis()
)
