package com.example.ecoidler.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecoidler.data.Repository

class ViewModelFactory(private val quoteRepository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return com.example.ecoidler.ui.DataViewModel(quoteRepository) as T
    }
}