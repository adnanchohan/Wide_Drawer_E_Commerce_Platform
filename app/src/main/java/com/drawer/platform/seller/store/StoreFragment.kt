package com.drawer.platform.seller.store

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drawer.platform.databinding.FragmentStoreBinding
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.loadFromPath
import com.drawer.platform.utils.show

class StoreFragment : Fragment() {
    private var _b: FragmentStoreBinding? = null
    private val b get() = _b!!
    private val vm: StoreViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentStoreBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.store.observe(viewLifecycleOwner) { store ->
            if (store != null) {
                b.layoutNoStore.hide()
                b.layoutStoreInfo.show()
                b.tvStoreName.text = store.storeName
                b.tvStoreDesc.text = store.description
                b.tvStoreRating.text = "★ ${store.rating}"
                b.tvStorePhone.text = store.phone.ifBlank { "No phone set" }
                b.ivStoreBanner.loadFromPath(store.bannerImagePath)
            } else {
                b.layoutNoStore.show()
                b.layoutStoreInfo.hide()
            }
        }
        b.btnCreateStore.setOnClickListener {
            startActivity(Intent(requireContext(), CreateStoreActivity::class.java))
        }
        b.btnEditStore.setOnClickListener {
            startActivity(Intent(requireContext(), CreateStoreActivity::class.java))
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
