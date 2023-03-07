package com.example.ecoidler.ui

import androidx.lifecycle.ViewModel
import com.example.ecoidler.data.Repository

class DataViewModel(private val repository: Repository) : ViewModel() {
    fun addWood() {
        val wood =
            (repository.getWoodGatherers().value ?: 0) + 10 * (repository.getWoodChoppers().value
                ?: 0)
        repository.addWood(wood)

        cutTree(repository.getWoodChoppers().value ?: 0)
    }

    fun addWoodGatherer() = repository.addWoodGatherer()
    fun addWoodChoppers() = repository.addWoodChoppers()
    fun getWood() = repository.getWood()
    fun getWoodGatherers() = repository.getWoodGatherers()
    fun getWoodChoppers() = repository.getWoodChoppers()
    fun cutTree(number: Int) = repository.cutTree(number)
    fun getTrees() = repository.getTrees()
    fun lost() = (getTrees().value ?: 0) < 0

    fun reset() = repository.reset()
}