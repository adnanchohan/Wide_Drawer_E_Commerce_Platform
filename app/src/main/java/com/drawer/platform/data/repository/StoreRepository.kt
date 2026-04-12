package com.drawer.platform.data.repository

import com.drawer.platform.data.db.StoreDao
import com.drawer.platform.data.model.StoreEntity
import kotlinx.coroutines.flow.Flow

class StoreRepository(private val storeDao: StoreDao) {

    suspend fun createStore(store: StoreEntity): Long = storeDao.insertStore(store)
    suspend fun updateStore(store: StoreEntity) = storeDao.updateStore(store)
    suspend fun getStoreBySellerId(sellerId: Long): StoreEntity? = storeDao.getStoreBySellerId(sellerId)
    suspend fun getStoreById(id: Long): StoreEntity? = storeDao.getStoreById(id)

    fun observeStoreBySellerId(sellerId: Long): Flow<StoreEntity?> =
        storeDao.observeStoreBySellerId(sellerId)

    fun getAllStores(): Flow<List<StoreEntity>> = storeDao.getAllStores()
}
