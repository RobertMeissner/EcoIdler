package com.example.ecoidler.ui

import androidx.lifecycle.ViewModel
import com.example.ecoidler.data.Repository

class DataViewModel(private val repository: Repository) : ViewModel() {
    fun addWood() {
        repository.addWood(repository.getWoodGatherers().value ?: 0)
    }

    fun addWoodGatherer() = repository.addWoodGatherer()
    fun getWood() = repository.getWood()
    fun getWoodGatherers() = repository.getWoodGatherers()
}