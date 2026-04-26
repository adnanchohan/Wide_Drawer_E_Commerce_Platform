package com.drawer.platform.seller.store

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drawer.platform.data.model.StoreEntity
import com.drawer.platform.ui.theme.DrawerWideTheme
import com.drawer.platform.utils.FileHelper
import com.drawer.platform.utils.SharedPrefManager

class CreateStoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val prefs = SharedPrefManager.getInstance(this)
            val vm: CreateStoreViewModel = viewModel()
            DrawerWideTheme(darkTheme = prefs.isDarkMode()) {
                CreateStoreScreen(vm) { finish() }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateStoreScreen(vm: CreateStoreViewModel, onBack: () -> Unit) {
        val existingState = vm.existing.observeAsState()
        val existing = existingState.value
        val isSavedState = vm.saved.observeAsState(false)
        val isSaved = isSavedState.value

        var name by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var address by remember { mutableStateOf("") }
        var bannerUri by remember { mutableStateOf<Uri?>(null) }

        LaunchedEffect(existing) {
            existing?.let {
                name = it.storeName
                desc = it.description
                phone = it.phone
                address = it.address
            }
        }

        if (isSaved) {
            LaunchedEffect(Unit) { onBack() }
        }

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            bannerUri = uri
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (existing != null) "Edit Store" else "Create Your Store") },
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (bannerUri == null && (existing?.bannerImagePath == null)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(48.dp))
                            Text("Pick Store Banner", style = MaterialTheme.typography.labelLarge)
                        }
                    } else {
                        Text("Banner Selected!", fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Store Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Store Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Store Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Store Address") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        val bannerPath = bannerUri?.let { FileHelper.copyImageFromUri(this@CreateStoreActivity, it, "store_banners") } ?: existing?.bannerImagePath
                        vm.saveStore(name, desc, phone, address, bannerPath)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Save Store Settings")
                }
            }
        }
    }
}
