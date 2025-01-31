package com.example.learnstack

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.learnstack.components.DrawerLayout
import com.example.learnstack.components.NoInternetPopup
import com.example.learnstack.navigation.AppNavigation
import com.example.learnstack.roomdata.AppDatabase
import com.example.learnstack.ui.theme.AppDarkTheme
import com.example.learnstack.ui.theme.AppLightTheme
import com.example.learnstack.utils.SharedPreferenceHelper
import com.example.learnstack.utils.isInternetAvailable
import com.example.learnstack.viewmodels.MainViewModel
import com.example.learnstack.viewmodels.MainViewModelFactory
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    @SuppressLint("StateFlowValueCalledInComposition")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)

        val sharedPreferenceHelper = SharedPreferenceHelper.getInstance(applicationContext)
        val appDatabase = AppDatabase.getInstance(applicationContext)
        val viewModelFactory = MainViewModelFactory(sharedPreferenceHelper, appDatabase)
        val viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        setContent {
            val navController = rememberNavController()
            val isLoggedIn by viewModel.isLoggedIn.collectAsState()
            var isInternetConnected by remember { mutableStateOf(true) } // Tracks internet connectivity
            var showPopup by remember { mutableStateOf(false) } // Controls popup visibility
            var darkTheme by remember { mutableStateOf(sharedPreferenceHelper.isDarkMode()) } // Load theme from preferences

            // Monitor Internet Connectivity
            LaunchedEffect(Unit) {
                var wasInternetConnected =
                    true // Track the previous state of the internet connection
                while (true) {
                    withContext(Dispatchers.IO) {
                        isInternetConnected = isInternetAvailable(applicationContext)
                    }

                    if (!isInternetConnected && wasInternetConnected) {
                        // Only show the popup when the connection is lost for the first time
                        showPopup = true
                    }

                    wasInternetConnected = isInternetConnected // Update the state

                    delay(2000) // Check every 2 seconds
                }
            }

            // Dynamic Theme Application
            MaterialTheme(colorScheme = if (darkTheme) AppDarkTheme else AppLightTheme) {
                // Show the popup if there is no internet connection
                NoInternetPopup(
                    isVisible = showPopup,
                    onDismiss = { showPopup = false } // Dismiss popup when user interacts
                )

                DrawerLayout(
                    navController = navController,
                    isLoggedIn = isLoggedIn,
                    disableDrawer = viewModel.isDrawerDisabled.value,
                    username = viewModel.username.value,
                    darkTheme = darkTheme, // Pass current theme state
                    onThemeToggle = {
                        darkTheme = !darkTheme
                        sharedPreferenceHelper.setDarkMode(darkTheme) // Save theme preference
                    },
                    onLogout = {
                        viewModel.onLogout()
                        sharedPreferenceHelper.setFirstLaunch(false)
                        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate("roadmapList") {
                            popUpTo("sliding_screen") { inclusive = true }
                        }
                    }
                ) { onMenuClicked ->
                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel,
                        onMenuClicked = onMenuClicked
                    )
                }
            }
        }

    }
}