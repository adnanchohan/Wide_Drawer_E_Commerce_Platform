package com.drawer.platform.data.db

import androidx.room.*
import com.drawer.platform.data.model.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: StoreEntity): Long

    @Update
    suspend fun updateStore(store: StoreEntity)

    @Query("SELECT * FROM stores WHERE sellerId = :sellerId LIMIT 1")
    suspend fun getStoreBySellerId(sellerId: Long): StoreEntity?

    @Query("SELECT * FROM stores WHERE sellerId = :sellerId LIMIT 1")
    fun observeStoreBySellerId(sellerId: Long): Flow<StoreEntity?>

    @Query("SELECT * FROM stores WHERE id = :id LIMIT 1")
    suspend fun getStoreById(id: Long): StoreEntity?

    @Query("SELECT * FROM stores ORDER BY createdAt DESC")
    fun getAllStores(): Flow<List<StoreEntity>>
}
