package com.drawer.platform.seller.products

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.drawer.platform.databinding.ActivityAddProductBinding
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.FileHelper
import com.drawer.platform.utils.showToast

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private val vm: AddProductViewModel by viewModels()
    private val selectedImageUris = mutableListOf<Uri>()
    private val savedImagePaths = mutableListOf<String>()
    private var savedVideoPath: String? = null
    private lateinit var imagePreviewAdapter: ProductImagePreviewAdapter

    private val pickImages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        uris.forEach { selectedImageUris.add(it) }
        imagePreviewAdapter.submitList(selectedImageUris.toList())
    }

    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.tvVideoStatus.text = "✓ Video selected"
            savedVideoPath = null
            savedImagePaths.add(it.toString()) // we'll process on save
            // store uri string temporarily, convert on save
            binding.tvVideoStatus.tag = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Product"

        // Category spinner
        val catAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            Constants.CATEGORIES.drop(1)) // drop "All"
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = catAdapter

        // Image preview RecyclerView
        imagePreviewAdapter = ProductImagePreviewAdapter { removeUri ->
            selectedImageUris.remove(removeUri)
            imagePreviewAdapter.submitList(selectedImageUris.toList())
        }
        binding.rvImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.adapter = imagePreviewAdapter

        binding.btnPickImages.setOnClickListener { pickImages.launch("image/*") }
        binding.btnPickVideo.setOnClickListener { pickVideo.launch("video/*") }

        binding.btnSaveProduct.setOnClickListener { saveProduct() }

        vm.saved.observe(this) { if (it) { showToast("Product added!"); finish() } }
        vm.error.observe(this) { showToast(it) }
    }

    private fun saveProduct() {
        val name = binding.etProductName.text.toString()
        val desc = binding.etProductDesc.text.toString()
        val price = binding.etPrice.text.toString()
        val stock = binding.etStock.text.toString()
        val category = binding.spinnerCategory.selectedItem?.toString() ?: "Other"

        // Copy images to internal storage
        val imagePaths = selectedImageUris.mapNotNull { FileHelper.copyImageFromUri(this, it) }

        // Copy video
        val videoUri = binding.tvVideoStatus.tag as? android.net.Uri
        val videoPath = videoUri?.let { FileHelper.copyVideoFromUri(this, it) }

        vm.saveProduct(name, desc, price, category, stock, imagePaths, videoPath)
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
