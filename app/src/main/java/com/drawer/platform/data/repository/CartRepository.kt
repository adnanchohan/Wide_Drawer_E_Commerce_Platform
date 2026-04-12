package com.drawer.platform.data.repository

import com.drawer.platform.data.db.CartDao
import com.drawer.platform.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    suspend fun addToCart(item: CartItemEntity): Long = cartDao.insertCartItem(item)
    suspend fun updateCartItem(item: CartItemEntity) = cartDao.updateCartItem(item)
    suspend fun removeFromCart(item: CartItemEntity) = cartDao.deleteCartItem(item)
    suspend fun clearCart(buyerId: Long) = cartDao.clearCart(buyerId)
    suspend fun getCartItemByProduct(buyerId: Long, productId: Long): CartItemEntity? =
        cartDao.getCartItemByProduct(buyerId, productId)

    fun getCartItems(buyerId: Long): Flow<List<CartItemEntity>> = cartDao.getCartItems(buyerId)
    fun getCartItemCount(buyerId: Long): Flow<Int> = cartDao.getCartItemCount(buyerId)
}
