package com.drawer.platform.buyer.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.CartItemEntity
import com.drawer.platform.data.model.OrderEntity
import com.drawer.platform.data.repository.CartRepository
import com.drawer.platform.data.repository.OrderRepository
import com.drawer.platform.data.repository.StoreRepository
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class CartViewModel(app: Application) : AndroidViewModel(app) {
    private val cartRepo = CartRepository(AppDatabase.getInstance(app).cartDao())
    private val orderRepo = OrderRepository(AppDatabase.getInstance(app).orderDao())
    private val storeRepo = StoreRepository(AppDatabase.getInstance(app).storeDao())
    private val prefs = SharedPrefManager.getInstance(app)
    private val buyerId = prefs.getUserId()

    val cartItems: LiveData<List<CartItemEntity>> = cartRepo.getCartItems(buyerId).asLiveData()
    val cartCount: LiveData<Int> = cartRepo.getCartItemCount(buyerId).asLiveData()

    private val _orderPlaced = MutableLiveData<Boolean>()
    val orderPlaced: LiveData<Boolean> = _orderPlaced

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun increaseQty(item: CartItemEntity) {
        viewModelScope.launch { cartRepo.updateCartItem(item.copy(quantity = item.quantity + 1)) }
    }

    fun decreaseQty(item: CartItemEntity) {
        viewModelScope.launch {
            if (item.quantity > 1) cartRepo.updateCartItem(item.copy(quantity = item.quantity - 1))
            else cartRepo.removeFromCart(item)
        }
    }

    fun removeItem(item: CartItemEntity) {
        viewModelScope.launch { cartRepo.removeFromCart(item) }
    }

    fun placeOrder(buyerAddress: String, buyerPhone: String) {
        val items = cartItems.value ?: return
        if (items.isEmpty()) { _error.value = "Your cart is empty"; return }
        if (buyerAddress.isBlank()) { _error.value = "Please enter your delivery address"; return }
        viewModelScope.launch {
            items.forEach { item ->
                val store = storeRepo.getStoreById(item.storeId)
                orderRepo.placeOrder(OrderEntity(
                    buyerId = buyerId, sellerId = item.sellerId,
                    productId = item.productId, productName = item.productName,
                    productPrice = item.productPrice, quantity = item.quantity,
                    buyerAddress = buyerAddress, sellerAddress = store?.address ?: "",
                    buyerPhone = buyerPhone, sellerPhone = store?.phone ?: "",
                    buyerName = prefs.getUserName(), storeName = item.storeName,
                    productImagePath = item.productImagePath
                ))
            }
            cartRepo.clearCart(buyerId)
            _orderPlaced.value = true
        }
    }

    fun getTotalPrice(): Double = cartItems.value?.sumOf { it.productPrice * it.quantity } ?: 0.0
}
