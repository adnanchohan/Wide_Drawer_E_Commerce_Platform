package com.drawer.platform.deliver.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.drawer.platform.databinding.FragmentMyDeliveriesBinding
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show

class MyDeliveriesFragment : Fragment() {
    private var _b: FragmentMyDeliveriesBinding? = null
    private val b get() = _b!!
    private val vm: MyDeliveriesViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentMyDeliveriesBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = DeliverOrderAdapter(
            showAccept = false,
            onAccept = {},
            onUpdateStatus = { order, status ->
                when (status) {
                    "PICKED_UP" -> vm.markPickedUp(order.id)
                    "DELIVERED" -> vm.markDelivered(order.id)
                }
            }
        )
        b.rvDeliveries.layoutManager = LinearLayoutManager(requireContext())
        b.rvDeliveries.adapter = adapter

        vm.myOrders.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            if (list.isEmpty()) { b.tvEmpty.show(); b.rvDeliveries.hide() }
            else { b.tvEmpty.hide(); b.rvDeliveries.show() }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
