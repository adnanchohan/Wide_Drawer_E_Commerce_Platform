package com.drawer.platform.seller.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import java.io.File
import com.drawer.platform.buyer.home.BuyerHomeActivity
import com.drawer.platform.onboarding.SplashActivity
import com.drawer.platform.seller.products.AddProductActivity
import com.drawer.platform.seller.store.CreateStoreActivity
import com.drawer.platform.seller.store.StoreViewModel
import com.drawer.platform.seller.products.ProductListViewModel
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.ui.theme.DrawerWideTheme
import com.drawer.platform.utils.SharedPrefManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

class SellerDashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val prefs = SharedPrefManager.getInstance(this)
            var darkTheme by remember { mutableStateOf(prefs.isDarkMode()) }

            DrawerWideTheme(darkTheme = darkTheme) {
                val storeVm: StoreViewModel = viewModel()
                SellerDashboardScreen(
                    storeVm = storeVm,
                    onThemeToggle = {
                        darkTheme = !darkTheme
                        prefs.setThemeMode(darkTheme)
                    },
                    onSwitchRole = {
                        startActivity(Intent(this, BuyerHomeActivity::class.java))
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
    fun SellerDashboardScreen(
        storeVm: StoreViewModel,
        onThemeToggle: () -> Unit,
        onSwitchRole: () -> Unit,
        onLogout: () -> Unit
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val prefs = SharedPrefManager.getInstance(this@SellerDashboardActivity)
        var selectedTab by remember { mutableStateOf(0) }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.75f)) {
                    DrawerHeader(prefs)
                    Spacer(modifier = Modifier.height(12.dp))
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                        label = { Text("Switch to Buyer Mode") },
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
            val storeState = storeVm.store.observeAsState()
            val titleText = storeState.value?.storeName ?: "Seller Dashboard"

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(titleText) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                            label = { Text("Overview") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(Icons.Default.Inventory, contentDescription = null) },
                            label = { Text("Products") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.ListAlt, contentDescription = null) },
                            label = { Text("Orders") }
                        )
                    }
                },
                floatingActionButton = {
                    if (selectedTab == 1) {
                        FloatingActionButton(onClick = { 
                            startActivity(Intent(this@SellerDashboardActivity, AddProductActivity::class.java)) 
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    when (selectedTab) {
                        0 -> SellerOverview(storeVm)
                        1 -> ProductsContent()
                        2 -> Text("Seller Orders")
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
                text = if (prefs.isLoggedIn()) prefs.getUserName() else "Seller Mode",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = if (prefs.isLoggedIn()) prefs.getUserEmail() else "Manage your store",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }

    @Composable
    fun SellerOverview(storeVm: StoreViewModel) {
        val storeState = storeVm.store.observeAsState()
        val store = storeState.value
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Business Overview", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            
            if (store == null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("You haven't set up your store yet.", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { 
                            context.startActivity(Intent(context, CreateStoreActivity::class.java))
                        }) {
                            Text("Create Store")
                        }
                    }
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard("Revenue", "$1,240", Modifier.weight(1f))
                    StatCard("Orders", "42", Modifier.weight(1f))
                }
            }
        }
    }

    @Composable
    fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
        Card(modifier = modifier, shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    @Composable
    fun ProductsContent() {
        val vm: ProductListViewModel = viewModel()
        val productsState = vm.products.observeAsState(emptyList())
        val products = productsState.value

        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No products added yet", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    SellerProductCard(product)
                }
            }
        }
    }

    @Composable
    fun SellerProductCard(product: ProductEntity) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                val firstImage = product.imagePaths.split(",").firstOrNull()?.trim()
                if (!firstImage.isNullOrEmpty()) {
                    AsyncImage(
                        model = File(firstImage),
                        contentDescription = product.name,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(product.category, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$${product.price}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text("Stock: ${product.stock}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
            }
        }
    }
}
