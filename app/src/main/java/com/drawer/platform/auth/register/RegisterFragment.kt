package com.drawer.platform.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.drawer.platform.auth.AuthActivity
import com.drawer.platform.databinding.FragmentRegisterBinding
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show
import com.drawer.platform.utils.showToast

class RegisterFragment : Fragment() {
    private var _b: FragmentRegisterBinding? = null
    private val b get() = _b!!
    private val vm: RegisterViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentRegisterBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mode = (activity as? AuthActivity)?.selectedMode ?: Constants.MODE_BUYER

        b.btnRegister.setOnClickListener {
            vm.register(
                b.etName.text.toString(), b.etEmail.text.toString(),
                b.etPassword.text.toString(), b.etPhone.text.toString(), mode
            )
        }
        b.tvGoLogin.setOnClickListener { findNavController().navigateUp() }

        vm.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RegisterState.Loading -> { b.progressBar.show(); b.btnRegister.isEnabled = false }
                is RegisterState.Success -> {
                    b.progressBar.hide(); b.btnRegister.isEnabled = true
                    requireContext().showToast("Account created! Please log in.")
                    findNavController().navigateUp()
                }
                is RegisterState.Error -> {
                    b.progressBar.hide(); b.btnRegister.isEnabled = true
                    requireContext().showToast(state.message)
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
