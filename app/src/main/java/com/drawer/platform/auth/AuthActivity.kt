package com.drawer.platform.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drawer.platform.ui.theme.DrawerWideTheme
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.SharedPrefManager
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drawer.platform.buyer.home.BuyerHomeActivity
import com.drawer.platform.seller.dashboard.SellerDashboardActivity

class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedMode = intent.getStringExtra(Constants.EXTRA_MODE) ?: Constants.MODE_BUYER

        setContent {
            val prefs = SharedPrefManager.getInstance(this)
            DrawerWideTheme(darkTheme = prefs.isDarkMode()) {
                val vm: AuthViewModel = viewModel()
                AuthScreen(vm, selectedMode) { 
                    if (selectedMode == Constants.MODE_SELLER) {
                        startActivity(Intent(this@AuthActivity, SellerDashboardActivity::class.java))
                    } else {
                        startActivity(Intent(this@AuthActivity, BuyerHomeActivity::class.java))
                    }
                    finish()
                }
            }
        }
    }

    @Composable
    fun AuthScreen(vm: AuthViewModel, mode: String, onAuthSuccess: () -> Unit) {
        var isLogin by remember { mutableStateOf(true) }
        val modeLabel = when (mode) {
            Constants.MODE_SELLER -> "🛍️ Seller Account"
            Constants.MODE_DELIVER -> "🚚 Delivery Partner"
            else -> "🛒 Buyer Account"
        }
        val authState = vm.authState.observeAsState(AuthState.Idle).value
        val context = LocalContext.current

        LaunchedEffect(authState) {
            when (authState) {
                is AuthState.Success -> onAuthSuccess()
                is AuthState.Error -> Toast.makeText(context, authState.msg, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = modeLabel,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (isLogin) "Welcome Back" else "Create Account",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (isLogin) "Login to your account to continue" else "Fill in your details to get started",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(40.dp))

            if (isLogin) {
                LoginFields(
                    onLogin = { e, p -> vm.login(e, p) },
                    isLoading = authState is AuthState.Loading
                )
            } else {
                RegisterFields(
                    onRegister = { n, e, p -> vm.register(n, e, p, mode) },
                    isLoading = authState is AuthState.Loading
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (isLogin) "Don't have an account?" else "Already have an account?")
                TextButton(onClick = { isLogin = !isLogin }) {
                    Text(if (isLogin) "Sign Up" else "Login")
                }
            }
        }
    }

    @Composable
    fun LoginFields(onLogin: (String, String) -> Unit, isLoading: Boolean) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = { onLogin(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Login")
            }
        }
    }

    @Composable
    fun RegisterFields(onRegister: (String, String, String) -> Unit, isLoading: Boolean) {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = { onRegister(name, email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Sign Up")
            }
        }
    }
}
