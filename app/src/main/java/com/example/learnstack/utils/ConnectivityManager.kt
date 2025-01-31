@file:Suppress("DEPRECATION")

package com.example.learnstack.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

// Function to check internet connectivity
@SuppressLint("ObsoleteSdkInt")
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    } else {
        // For older versions of Android
        connectivityManager.activeNetworkInfo?.isConnected == true
    }
}
