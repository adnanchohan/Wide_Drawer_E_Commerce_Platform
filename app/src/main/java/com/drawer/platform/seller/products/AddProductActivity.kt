package com.drawer.platform.seller.products

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drawer.platform.ui.theme.DrawerWideTheme
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.FileHelper
import com.drawer.platform.utils.SharedPrefManager

class AddProductActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val prefs = SharedPrefManager.getInstance(this)
            val vm: AddProductViewModel = viewModel()
            DrawerWideTheme(darkTheme = prefs.isDarkMode()) {
                AddProductScreen(vm) { finish() }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddProductScreen(vm: AddProductViewModel, onBack: () -> Unit) {
        var name by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var stock by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf(Constants.CATEGORIES[1]) }
        var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
        var selectedVideo by remember { mutableStateOf<Uri?>(null) }
        
        val isSavedState = vm.saved.observeAsState(false)
        val isSaved = isSavedState.value
        if (isSaved) {
            LaunchedEffect(Unit) { onBack() }
        }
        val errorState = vm.error.observeAsState(null)
        val context = LocalContext.current
        LaunchedEffect(errorState.value) {
            errorState.value?.let { 
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            selectedImages = selectedImages + uris
        }
        val videoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedVideo = uri
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add Product") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Images & Media", style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        Surface(
                            modifier = Modifier.size(100.dp).clickable { imageLauncher.launch("image/*") },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(32.dp))
                        }
                    }
                    items(selectedImages) { uri ->
                        Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                            IconButton(onClick = { selectedImages = selectedImages - uri }, modifier = Modifier.align(Alignment.TopEnd)) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                OutlinedCard(onClick = { videoLauncher.launch("video/*") }, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Movie, contentDescription = null)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(if (selectedVideo == null) "Add Product Reel (Video)" else "✓ Video Attached", modifier = Modifier.weight(1f))
                    }
                }

                Divider()

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Product Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price ($)") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, modifier = Modifier.weight(1f))
                }

                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        Constants.CATEGORIES.drop(1).forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = { selectedCategory = category; expanded = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        val imagePaths = selectedImages.mapNotNull { FileHelper.copyImageFromUri(this@AddProductActivity, it) }
                        val videoPath = selectedVideo?.let { FileHelper.copyVideoFromUri(this@AddProductActivity, it) }
                        vm.saveProduct(name, desc, price, selectedCategory, stock, imagePaths, videoPath)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Publish Product")
                }
            }
        }
    }
}

fun Modifier.clickable(onClick: () -> Unit): Modifier = this.then(Modifier.clickable(onClick = onClick))
