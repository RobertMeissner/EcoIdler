package com.example.ecoidler.utils

import com.example.ecoidler.ui.DataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    viewModel { DataViewModel() }
}
