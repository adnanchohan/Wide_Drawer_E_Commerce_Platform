package com.drawer.platform.deliver.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.drawer.platform.databinding.ActivityDeliverRegisterBinding
import com.drawer.platform.deliver.orders.DeliverOrdersActivity
import com.drawer.platform.utils.FileHelper
import com.drawer.platform.utils.loadFromUri
import com.drawer.platform.utils.showToast

class DeliverRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliverRegisterBinding
    private val vm: DeliverRegisterViewModel by viewModels()
    private var idProofUri: Uri? = null

    private val pickIdProof = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { idProofUri = it; binding.ivIdProof.loadFromUri(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliverRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Delivery Profile"

        binding.btnPickIdProof.setOnClickListener { pickIdProof.launch("image/*") }

        binding.btnSave.setOnClickListener {
            val vehicle = binding.etVehicleType.text.toString()
            val area = binding.etCoverageArea.text.toString()
            val idPath = idProofUri?.let { FileHelper.copyImageFromUri(this, it, "id_proofs") }
            vm.saveProfile(vehicle, area, idPath)
        }

        vm.saved.observe(this) {
            if (it) {
                showToast("Profile saved!")
                startActivity(Intent(this, DeliverOrdersActivity::class.java)
                    .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
            }
        }
        vm.error.observe(this) { showToast(it) }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
