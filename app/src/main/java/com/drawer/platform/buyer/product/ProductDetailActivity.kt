package com.drawer.platform.buyer.product

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.ui.theme.DrawerWideTheme
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.SharedPrefManager
import com.drawer.platform.utils.toPrice

class ProductDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productId = intent.getLongExtra(Constants.EXTRA_PRODUCT_ID, -1L)
        if (productId == -1L) { finish(); return }

        setContent {
            val prefs = SharedPrefManager.getInstance(this)
            val vm: ProductDetailViewModel = viewModel()
            vm.loadProduct(productId)

            DrawerWideTheme(darkTheme = prefs.isDarkMode()) {
                ProductDetailScreen(vm) { finish() }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProductDetailScreen(vm: ProductDetailViewModel, onBack: () -> Unit) {
        val productState = vm.product.observeAsState()
        val product = productState.value
        val storeState = vm.store.observeAsState()
        val store = storeState.value

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(product?.name ?: "Loading...") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            bottomBar = {
                Surface(tonalElevation = 8.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Price", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = product?.price?.toPrice() ?: "$0.00",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Button(
                            onClick = { product?.let { vm.addToCart(it) } },
                            modifier = Modifier
                                .height(56.dp)
                                .weight(1f),
                            shape = MaterialTheme.shapes.medium,
                            enabled = (product?.stock ?: 0) > 0
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add to Cart")
                        }
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Placeholder for Image Gallery
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📷 Product Images", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = product?.name ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "📦 ${product?.category}",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Description", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product?.description ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Sold by", style = MaterialTheme.typography.labelSmall)
                                Text(store?.storeName ?: "Unknown Store", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
