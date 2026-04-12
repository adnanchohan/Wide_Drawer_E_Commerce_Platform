package com.drawer.platform.buyer.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.drawer.platform.R
import com.drawer.platform.buyer.product.ProductDetailActivity
import com.drawer.platform.buyer.reels.ReelsActivity
import com.drawer.platform.databinding.FragmentHomeBinding
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show

class HomeFragment : Fragment() {
    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!
    private val vm: HomeViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentHomeBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Category chips
        val categoryAdapter = CategoryChipAdapter(Constants.CATEGORIES) { cat ->
            vm.selectCategory(cat)
        }
        b.rvCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        b.rvCategories.adapter = categoryAdapter

        // Products grid
        val productAdapter = BuyerProductCardAdapter { product ->
            startActivity(Intent(requireContext(), ProductDetailActivity::class.java)
                .putExtra(Constants.EXTRA_PRODUCT_ID, product.id))
        }
        b.rvProducts.layoutManager = GridLayoutManager(context, 2)
        b.rvProducts.adapter = productAdapter

        vm.filteredProducts.observe(viewLifecycleOwner) { list ->
            productAdapter.submitList(list)
            if (list.isEmpty()) { b.tvEmpty.show(); b.rvProducts.hide() }
            else { b.tvEmpty.hide(); b.rvProducts.show() }
        }

        // Navigate to Search tab via NavController
        b.etSearch.setOnClickListener {
            findNavController().navigate(R.id.nav_search)
        }

        // Reels button
        b.btnReels.setOnClickListener {
            startActivity(Intent(requireContext(), ReelsActivity::class.java))
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
