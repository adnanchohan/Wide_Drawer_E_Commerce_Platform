package com.drawer.platform.buyer.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.drawer.platform.databinding.FragmentCartBinding
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show
import com.drawer.platform.utils.showToast
import com.drawer.platform.utils.toPrice

class CartFragment : Fragment() {
    private var _b: FragmentCartBinding? = null
    private val b get() = _b!!
    private val vm: CartViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentCartBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CartItemAdapter(
            onIncrease = { vm.increaseQty(it) },
            onDecrease = { vm.decreaseQty(it) },
            onRemove = { vm.removeItem(it) }
        )
        b.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        b.rvCartItems.adapter = adapter

        vm.cartItems.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            val total = list.sumOf { it.productPrice * it.quantity }
            b.tvTotal.text = "Total: ${total.toPrice()}"
            if (list.isEmpty()) {
                b.layoutEmpty.show(); b.layoutCheckout.hide(); b.rvCartItems.hide()
            } else {
                b.layoutEmpty.hide(); b.layoutCheckout.show(); b.rvCartItems.show()
            }
        }

        b.btnPlaceOrder.setOnClickListener {
            val address = b.etAddress.text.toString()
            val phone = b.etPhone.text.toString()
            vm.placeOrder(address, phone)
        }

        vm.orderPlaced.observe(viewLifecycleOwner) { placed ->
            if (placed) {
                requireContext().showToast("Order placed successfully! 🎉")
                b.etAddress.text?.clear()
                b.etPhone.text?.clear()
            }
        }
        vm.error.observe(viewLifecycleOwner) { requireContext().showToast(it) }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
