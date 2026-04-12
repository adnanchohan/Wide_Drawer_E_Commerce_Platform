package com.drawer.platform.data.db

import androidx.room.*
import com.drawer.platform.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity): Long

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE buyerId = :buyerId ORDER BY addedAt DESC")
    fun getCartItems(buyerId: Long): Flow<List<CartItemEntity>>

    @Query("DELETE FROM cart_items WHERE buyerId = :buyerId")
    suspend fun clearCart(buyerId: Long)

    @Query("SELECT COUNT(*) FROM cart_items WHERE buyerId = :buyerId")
    fun getCartItemCount(buyerId: Long): Flow<Int>

    @Query("SELECT * FROM cart_items WHERE buyerId = :buyerId AND productId = :productId LIMIT 1")
    suspend fun getCartItemByProduct(buyerId: Long, productId: Long): CartItemEntity?
}
