package com.drawer.platform.buyer.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.drawer.platform.buyer.home.BuyerProductCardAdapter
import com.drawer.platform.buyer.product.ProductDetailActivity
import com.drawer.platform.databinding.FragmentSearchBinding
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show

class SearchFragment : Fragment() {
    private var _b: FragmentSearchBinding? = null
    private val b get() = _b!!
    private val vm: SearchViewModel by viewModels()

    companion object {
        fun newInstance(query: String = "") = SearchFragment().apply {
            arguments = Bundle().apply { putString("query", query) }
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSearchBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val initQuery = arguments?.getString("query") ?: ""
        if (initQuery.isNotBlank()) {
            b.etSearch.setText(initQuery)
            vm.search(initQuery)
        }

        val adapter = BuyerProductCardAdapter { product ->
            startActivity(Intent(requireContext(), ProductDetailActivity::class.java)
                .putExtra(Constants.EXTRA_PRODUCT_ID, product.id))
        }
        b.rvResults.layoutManager = GridLayoutManager(requireContext(), 2)
        b.rvResults.adapter = adapter

        b.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { vm.search(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, st: Int, co: Int, af: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, co: Int) {}
        })

        b.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        vm.results.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            if (list.isEmpty()) { b.tvEmpty.show(); b.rvResults.hide() }
            else { b.tvEmpty.hide(); b.rvResults.show() }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
