package com.drawer.platform.deliver.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drawer.platform.databinding.FragmentDeliverProfileBinding
import com.drawer.platform.deliver.register.DeliverRegisterActivity
import com.drawer.platform.onboarding.ModeSelectionActivity

class DeliverProfileFragment : Fragment() {
    private var _b: FragmentDeliverProfileBinding? = null
    private val b get() = _b!!
    private val vm: DeliverProfileViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentDeliverProfileBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                b.tvName.text = user.name
                b.tvEmail.text = user.email
                b.tvPhone.text = user.phone
                b.tvMode.text = "🚚 Delivery Partner"
                b.tvVehicle.text = "Vehicle: ${user.vehicleType.ifBlank { "Not set" }}"
                b.tvArea.text = "Area: ${user.address.ifBlank { "Not set" }}"
            }
        }
        b.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), DeliverRegisterActivity::class.java))
        }
        b.btnLogout.setOnClickListener {
            vm.logout {
                startActivity(Intent(requireContext(), ModeSelectionActivity::class.java)
                    .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
