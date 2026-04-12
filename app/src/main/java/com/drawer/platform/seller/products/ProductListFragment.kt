package com.drawer.platform.seller.products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.drawer.platform.databinding.FragmentProductListBinding
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show

class ProductListFragment : Fragment() {
    private var _b: FragmentProductListBinding? = null
    private val b get() = _b!!
    private val vm: ProductListViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentProductListBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SellerProductAdapter { product ->
            // future: open product edit screen
        }
        b.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        b.rvProducts.adapter = adapter

        vm.products.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            if (list.isEmpty()) { b.tvEmpty.show(); b.rvProducts.hide() }
            else { b.tvEmpty.hide(); b.rvProducts.show() }
        }

        b.fabAddProduct.setOnClickListener {
            startActivity(Intent(requireContext(), AddProductActivity::class.java))
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
