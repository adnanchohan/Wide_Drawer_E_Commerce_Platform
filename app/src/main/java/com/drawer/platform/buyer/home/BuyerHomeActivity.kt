package com.drawer.platform.buyer.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drawer.platform.auth.AuthActivity
import com.drawer.platform.buyer.cart.CartViewModel
import com.drawer.platform.buyer.product.ProductDetailActivity
import com.drawer.platform.data.model.CartItemEntity
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.onboarding.SplashActivity
import com.drawer.platform.buyer.reels.ReelsActivity
import com.drawer.platform.seller.dashboard.SellerDashboardActivity
import com.drawer.platform.ui.theme.DrawerWideTheme
import coil.compose.AsyncImage
import java.io.File
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.SharedPrefManager
import com.drawer.platform.utils.toPrice
import kotlinx.coroutines.launch

class BuyerHomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val prefs = SharedPrefManager.getInstance(this)
            var darkTheme by remember { mutableStateOf(prefs.isDarkMode()) }

            DrawerWideTheme(darkTheme = darkTheme) {
                BuyerHomeScreen(
                    onThemeToggle = {
                        darkTheme = !darkTheme
                        prefs.setThemeMode(darkTheme)
                    },
                    onSwitchRole = {
                        if (prefs.isLoggedIn()) {
                            startActivity(Intent(this, SellerDashboardActivity::class.java))
                        } else {
                            startActivity(Intent(this, AuthActivity::class.java).putExtra(Constants.EXTRA_MODE, Constants.MODE_SELLER))
                        }
                        finish()
                    },
                    onLogout = {
                        prefs.clearSession()
                        startActivity(Intent(this, SplashActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BuyerHomeScreen(
        onThemeToggle: () -> Unit,
        onSwitchRole: () -> Unit,
        onLogout: () -> Unit
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val prefs = SharedPrefManager.getInstance(this@BuyerHomeActivity)
        var selectedTab by remember { mutableStateOf(0) }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.75f)) {
                    DrawerHeader(prefs)
                    Spacer(modifier = Modifier.height(12.dp))
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Store, contentDescription = null) },
                        label = { Text("Switch to Seller Mode") },
                        selected = false,
                        onClick = onSwitchRole
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.BrightnessMedium, contentDescription = null) },
                        label = { Text(if (prefs.isDarkMode()) "Light Mode" else "Dark Mode") },
                        selected = false,
                        onClick = onThemeToggle
                    )
                    if (prefs.isLoggedIn()) {
                        NavigationDrawerItem(
                            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                            label = { Text("Logout") },
                            selected = false,
                            onClick = onLogout
                        )
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("E-Baazar") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { startActivity(Intent(this@BuyerHomeActivity, ReelsActivity::class.java)) }) {
                                Icon(Icons.Default.PlayCircle, contentDescription = "Reels")
                            }
                            IconButton(onClick = { /* Open Search */ }) {
                                Icon(Icons.Default.Search, contentDescription = null)
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(Icons.Default.Home, contentDescription = null) },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(Icons.Default.Category, contentDescription = null) },
                            label = { Text("Explore") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                            label = { Text("Cart") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            icon = { Icon(Icons.Default.Person, contentDescription = null) },
                            label = { Text("Profile") }
                        )
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    when (selectedTab) {
                        0 -> HomeContent()
                        1 -> ExploreContent()
                        2 -> CartContent()
                        3 -> ProfileContent(prefs, onLogout)
                    }
                }
            }
        }
    }

    @Composable
    fun DrawerHeader(prefs: SharedPrefManager) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp)
        ) {
            Text("✨", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (prefs.isLoggedIn()) prefs.getUserName() else "Guest User",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = if (prefs.isLoggedIn()) prefs.getUserEmail() else "Join DrawerWide today",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }

    @Composable
    fun HomeContent() {
        val vm: HomeViewModel = viewModel()
        val productsState = vm.filteredProducts.observeAsState(emptyList<ProductEntity>())
        val products = productsState.value

        Column(modifier = Modifier.fillMaxSize()) {
            LazyRow(
                modifier = Modifier.padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(Constants.CATEGORIES) { category ->
                    CategoryChip(category) { vm.selectCategory(category) }
                }
            }
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Top Picks for You", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(products) { product ->
                    ProductCard(product.name, product.price, product.category, product.imagePaths) {
                        startActivity(Intent(this@BuyerHomeActivity, ProductDetailActivity::class.java)
                            .putExtra(Constants.EXTRA_PRODUCT_ID, product.id))
                    }
                }
            }
        }
    }

    @Composable
    fun CartContent() {
        val vm: CartViewModel = viewModel()
        val cartItemsState = vm.cartItems.observeAsState(emptyList<CartItemEntity>())
        val cartItems = cartItemsState.value

        if (cartItems.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(cartItems) { item ->
                        CartItemRow(item.productName, item.productPrice, item.quantity)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                val total = cartItems.sumOf { it.productPrice * it.quantity }
                Text("Total: ${total.toPrice()}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Checkout */ }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                    Text("Proceed to Checkout")
                }
            }
        }
    }

    @Composable
    fun ExploreContent() {
        LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { Text("Explore Categories", style = MaterialTheme.typography.titleLarge) }
            items(Constants.CATEGORIES) { cat ->
                Card(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(cat, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    @Composable
    fun ProfileContent(prefs: SharedPrefManager, onLogout: () -> Unit) {
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(100.dp).clip(RoundedCornerShape(50.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Text(prefs.getUserName().take(1), fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(prefs.getUserName(), style = MaterialTheme.typography.headlineSmall)
            Text(prefs.getUserEmail(), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            
            Spacer(modifier = Modifier.height(40.dp))
            ProfileMenuItem(Icons.Default.ShoppingBag, "My Orders")
            ProfileMenuItem(Icons.Default.Favorite, "Wishlist")
            ProfileMenuItem(Icons.Default.Settings, "Settings")
            Spacer(modifier = Modifier.weight(1f))
            if (prefs.isLoggedIn()) {
                OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                    Text("Logout")
                }
            } else {
                Button(onClick = { startActivity(Intent(this@BuyerHomeActivity, AuthActivity::class.java)) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Sign In")
                }
            }
        }
    }

    @Composable
    fun ProfileMenuItem(icon: ImageVector, label: String) {
        Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null)
        }
    }

    @Composable
    fun CategoryChip(name: String, onClick: () -> Unit) {
        Surface(
            modifier = Modifier.padding(end = 8.dp).clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = name,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Medium
            )
        }
    }

    @Composable
    fun ProductCard(name: String, price: Double, category: String, imagePaths: String?, onClick: () -> Unit) {
        Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            shape = MaterialTheme.shapes.large
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                val firstImage = imagePaths?.split(",")?.firstOrNull()?.trim()
                if (!firstImage.isNullOrEmpty()) {
                    AsyncImage(
                        model = File(firstImage),
                        contentDescription = name,
                        modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(category, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(price.toPrice(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }

    @Composable
    fun CartItemRow(name: String, price: Double, qty: Int) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant))
            Spacer(modifier = Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.SemiBold)
                Text(price.toPrice(), color = MaterialTheme.colorScheme.primary)
            }
            Text("x$qty", fontWeight = FontWeight.Bold)
        }
    }
}
