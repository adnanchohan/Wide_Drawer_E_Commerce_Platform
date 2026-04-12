package com.drawer.platform.seller.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drawer.platform.databinding.FragmentSellerProfileBinding
import com.drawer.platform.onboarding.ModeSelectionActivity

class SellerProfileFragment : Fragment() {
    private var _b: FragmentSellerProfileBinding? = null
    private val b get() = _b!!
    private val vm: SellerProfileViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSellerProfileBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                b.tvName.text = user.name
                b.tvEmail.text = user.email
                b.tvPhone.text = user.phone
                b.tvMode.text = "🏪 Seller"
            }
        }
        b.btnLogout.setOnClickListener {
            vm.logout {
                val intent = Intent(requireContext(), ModeSelectionActivity::class.java)
                    .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
