package com.example.ecoidler.utils

import com.example.ecoidler.data.FakeDatabase
import com.example.ecoidler.data.Repository
import com.example.ecoidler.ui.ViewModelFactory


object InjectorUtils {
    fun provideQuotesViewModelFactory(): ViewModelFactory {
        val repository = Repository.getInstance(FakeDatabase.getInstance().fakeDao)
        return ViewModelFactory(repository)
    }
}