package com.drawer.platform.seller.store

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.drawer.platform.databinding.ActivityCreateStoreBinding
import com.drawer.platform.utils.FileHelper
import com.drawer.platform.utils.loadFromPath
import com.drawer.platform.utils.loadFromUri
import com.drawer.platform.utils.showToast

class CreateStoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoreBinding
    private val vm: CreateStoreViewModel by viewModels()
    private var bannerUri: Uri? = null
    private var savedBannerPath: String? = null

    private val pickBanner = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { bannerUri = it; binding.ivBanner.loadFromUri(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        vm.existing.observe(this) { store ->
            if (store != null) {
                binding.toolbar.title = "Edit Store"
                binding.etStoreName.setText(store.storeName)
                binding.etStoreDesc.setText(store.description)
                binding.etStorePhone.setText(store.phone)
                binding.etStoreAddress.setText(store.address)
                savedBannerPath = store.bannerImagePath
                binding.ivBanner.loadFromPath(store.bannerImagePath)
            } else {
                binding.toolbar.title = "Create Your Store"
            }
        }

        binding.ivBanner.setOnClickListener { pickBanner.launch("image/*") }
        binding.btnPickBanner.setOnClickListener { pickBanner.launch("image/*") }

        binding.btnSave.setOnClickListener {
            val name = binding.etStoreName.text.toString().trim()
            val desc = binding.etStoreDesc.text.toString().trim()
            if (name.isBlank()) { showToast("Store name is required"); return@setOnClickListener }
            val bannerPath = bannerUri?.let { FileHelper.copyImageFromUri(this, it, "store_banners") } ?: savedBannerPath
            vm.saveStore(name, desc, binding.etStorePhone.text.toString().trim(),
                binding.etStoreAddress.text.toString().trim(), bannerPath)
        }

        vm.saved.observe(this) { if (it) { showToast("Store saved!"); finish() } }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
