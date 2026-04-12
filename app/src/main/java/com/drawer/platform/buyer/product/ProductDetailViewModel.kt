package com.drawer.platform.buyer.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.drawer.platform.data.db.AppDatabase
import com.drawer.platform.data.model.CartItemEntity
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.data.model.StoreEntity
import com.drawer.platform.data.repository.CartRepository
import com.drawer.platform.data.repository.ProductRepository
import com.drawer.platform.data.repository.StoreRepository
import com.drawer.platform.utils.FileHelper
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class ProductDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val productRepo = ProductRepository(AppDatabase.getInstance(app).productDao())
    private val storeRepo = StoreRepository(AppDatabase.getInstance(app).storeDao())
    private val cartRepo = CartRepository(AppDatabase.getInstance(app).cartDao())
    private val prefs = SharedPrefManager.getInstance(app)

    private val _product = MutableLiveData<ProductEntity?>()
    val product: LiveData<ProductEntity?> = _product

    private val _store = MutableLiveData<StoreEntity?>()
    val store: LiveData<StoreEntity?> = _store

    private val _cartMessage = MutableLiveData<String>()
    val cartMessage: LiveData<String> = _cartMessage

    fun loadProduct(id: Long) {
        viewModelScope.launch {
            val p = productRepo.getProductById(id)
            _product.value = p
            p?.let { _store.value = storeRepo.getStoreById(it.storeId) }
        }
    }

    fun addToCart(product: ProductEntity) {
        viewModelScope.launch {
            val buyerId = prefs.getUserId()
            val existing = cartRepo.getCartItemByProduct(buyerId, product.id)
            val store = _store.value
            if (existing != null) {
                cartRepo.updateCartItem(existing.copy(quantity = existing.quantity + 1))
                _cartMessage.value = "Quantity updated in cart"
            } else {
                cartRepo.addToCart(CartItemEntity(
                    buyerId = buyerId, productId = product.id,
                    productName = product.name, productPrice = product.price,
                    productImagePath = FileHelper.getFirstImagePath(product.imagePaths) ?: "",
                    storeName = store?.storeName ?: "", sellerId = product.sellerId,
                    storeId = product.storeId
                ))
                _cartMessage.value = "Added to cart!"
            }
        }
    }
}
