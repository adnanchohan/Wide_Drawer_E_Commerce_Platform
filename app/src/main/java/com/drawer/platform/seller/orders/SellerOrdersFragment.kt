package com.drawer.platform.seller.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.drawer.platform.databinding.FragmentSellerOrdersBinding
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show

class SellerOrdersFragment : Fragment() {
    private var _b: FragmentSellerOrdersBinding? = null
    private val b get() = _b!!
    private val vm: SellerOrdersViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSellerOrdersBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SellerOrderAdapter(
            onAccept = { order -> vm.updateStatus(order.id, "ACCEPTED") },
            onCancel = { order -> vm.updateStatus(order.id, "CANCELLED") }
        )
        b.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        b.rvOrders.adapter = adapter

        vm.orders.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            if (list.isEmpty()) { b.tvEmpty.show(); b.rvOrders.hide() }
            else { b.tvEmpty.hide(); b.rvOrders.show() }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
