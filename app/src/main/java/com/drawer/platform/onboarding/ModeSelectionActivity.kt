package com.drawer.platform.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drawer.platform.auth.AuthActivity
import com.drawer.platform.ui.theme.DrawerWideTheme
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.SharedPrefManager

class ModeSelectionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val prefs = SharedPrefManager.getInstance(this)
            DrawerWideTheme(darkTheme = prefs.isDarkMode()) {
                ModeSelectionScreen()
            }
        }
    }

    @Composable
    fun ModeSelectionScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Choose Your Role",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Select how you want to use DrawerWide",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))

            RoleCard("🛍️", "I want to Buy", "Browse and purchase products") {
                openAuth(Constants.MODE_BUYER)
            }
            Spacer(modifier = Modifier.height(16.dp))
            RoleCard("🏪", "I want to Sell", "List products and manage your store") {
                openAuth(Constants.MODE_SELLER)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RoleCard(emoji: String, title: String, subtitle: String, onClick: () -> Unit) {
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(emoji, fontSize = 40.sp)
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(subtitle, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }
    }

    private fun openAuth(mode: String) {
        startActivity(Intent(this, AuthActivity::class.java).putExtra(Constants.EXTRA_MODE, mode))
    }
}
