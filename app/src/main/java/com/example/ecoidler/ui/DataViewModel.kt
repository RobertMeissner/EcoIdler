package com.example.ecoidler.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ecoidler.data.FakeDatabase
import com.example.ecoidler.data.Repository

class DataViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: Repository = Repository.getInstance(FakeDatabase.getInstance().fakeDao)
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

    fun score(): Int {
        return (getTrees().value ?: 0) * 100 + (getWood().value ?: 0)
    }
}