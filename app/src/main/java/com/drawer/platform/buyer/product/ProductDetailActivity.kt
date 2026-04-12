package com.drawer.platform.buyer.product

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.drawer.platform.databinding.ActivityProductDetailBinding
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.FileHelper
import com.drawer.platform.utils.showToast
import com.drawer.platform.utils.toPrice

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val vm: ProductDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val productId = intent.getLongExtra(Constants.EXTRA_PRODUCT_ID, -1L)
        if (productId == -1L) { finish(); return }
        vm.loadProduct(productId)

        val imageAdapter = ProductImageGalleryAdapter()
        binding.vpProductImages.adapter = imageAdapter
        binding.indicator.setViewPager(binding.vpProductImages)

        vm.product.observe(this) { product ->
            if (product == null) { finish(); return@observe }
            supportActionBar?.title = product.name
            binding.tvProductName.text = product.name
            binding.tvPrice.text = product.price.toPrice()
            binding.tvDescription.text = product.description
            binding.tvCategory.text = "📦 ${product.category}"
            binding.tvStock.text = if (product.stock > 0) "In Stock (${product.stock})" else "Out of Stock"

            val images = FileHelper.parseImagePaths(product.imagePaths)
            imageAdapter.submitList(images)
            if (images.isEmpty()) binding.indicator.visibility = android.view.View.GONE

            binding.btnAddToCart.isEnabled = product.stock > 0
            binding.btnAddToCart.setOnClickListener { vm.addToCart(product) }
        }

        vm.store.observe(this) { store ->
            binding.tvStoreName.text = store?.storeName ?: "Unknown Store"
        }

        vm.cartMessage.observe(this) { msg -> showToast(msg) }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
