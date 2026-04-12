package com.drawer.platform.data.repository

import com.drawer.platform.data.db.ProductDao
import com.drawer.platform.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    suspend fun addProduct(product: ProductEntity): Long = productDao.insertProduct(product)
    suspend fun updateProduct(product: ProductEntity) = productDao.updateProduct(product)
    suspend fun deleteProduct(product: ProductEntity) = productDao.deleteProduct(product)
    suspend fun getProductById(id: Long): ProductEntity? = productDao.getProductById(id)

    fun getProductsBySeller(sellerId: Long): Flow<List<ProductEntity>> =
        productDao.getProductsBySeller(sellerId)

    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> =
        productDao.getProductsByCategory(category)

    fun searchProducts(query: String): Flow<List<ProductEntity>> =
        productDao.searchProducts(query)

    fun getProductsWithVideos(): Flow<List<ProductEntity>> =
        productDao.getProductsWithVideos()
}
