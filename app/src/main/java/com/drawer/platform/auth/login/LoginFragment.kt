package com.drawer.platform.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.drawer.platform.R
import com.drawer.platform.auth.AuthActivity
import com.drawer.platform.buyer.home.BuyerHomeActivity
import com.drawer.platform.databinding.FragmentLoginBinding
import com.drawer.platform.deliver.orders.DeliverOrdersActivity
import com.drawer.platform.seller.dashboard.SellerDashboardActivity
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.SharedPrefManager
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show
import com.drawer.platform.utils.showToast

class LoginFragment : Fragment() {
    private var _b: FragmentLoginBinding? = null
    private val b get() = _b!!
    private val vm: LoginViewModel by viewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentLoginBinding.inflate(i, c, false); return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mode = (activity as? AuthActivity)?.selectedMode ?: Constants.MODE_BUYER

        b.btnLogin.setOnClickListener {
            vm.login(b.etEmail.text.toString(), b.etPassword.text.toString())
        }
        b.tvGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        vm.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginState.Loading -> { b.progressBar.show(); b.btnLogin.isEnabled = false }
                is LoginState.Success -> {
                    b.progressBar.hide(); b.btnLogin.isEnabled = true
                    SharedPrefManager.getInstance(requireContext())
                        .saveUserSession(state.user.id, state.user.mode, state.user.name, state.user.email)
                    navigateToDashboard(state.user.mode)
                }
                is LoginState.Error -> {
                    b.progressBar.hide(); b.btnLogin.isEnabled = true
                    requireContext().showToast(state.message)
                }
                else -> {}
            }
        }
    }

    private fun navigateToDashboard(mode: String) {
        val intent = when (mode) {
            Constants.MODE_SELLER -> Intent(requireContext(), SellerDashboardActivity::class.java)
            Constants.MODE_DELIVER -> Intent(requireContext(), DeliverOrdersActivity::class.java)
            else -> Intent(requireContext(), BuyerHomeActivity::class.java)
        }.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
        startActivity(intent)
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
