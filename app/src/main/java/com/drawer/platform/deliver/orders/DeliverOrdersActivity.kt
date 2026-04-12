package com.drawer.platform.deliver.orders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.drawer.platform.R
import com.drawer.platform.databinding.ActivityDeliverOrdersBinding

class DeliverOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliverOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliverOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_deliver) as NavHostFragment
        binding.bottomNavDeliver.setupWithNavController(navHost.navController)
    }
}
