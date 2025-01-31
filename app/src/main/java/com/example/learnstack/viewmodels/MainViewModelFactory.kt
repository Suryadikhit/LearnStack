package com.example.learnstack.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnstack.roomdata.AppDatabase
import com.example.learnstack.utils.SharedPreferenceHelper

class MainViewModelFactory(
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val appDatabase: AppDatabase,  // Add appDatabase as a parameter

) : ViewModelProvider.Factory {
    @SuppressLint("NewApi")
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(sharedPreferenceHelper, appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
