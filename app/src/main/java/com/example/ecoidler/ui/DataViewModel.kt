package com.example.ecoidler.ui

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoidler.data.FakeDatabase
import com.example.ecoidler.data.Repository
import kotlinx.coroutines.launch

class DataViewModel(application: Application) : AndroidViewModel(application) {
    private val _wood = mutableStateOf(0)
    val wood: State<Int> = _wood

    private var repository: Repository = Repository.getInstance(FakeDatabase.getInstance().fakeDao)
    fun addWood() {
        val choppedWood =
            (repository.getWoodGatherers().value ?: 0) + 10 * (repository.getWoodChoppers().value
                ?: 0)
        repository.addWood(choppedWood)
        // Todo connect _wood better with repository wood
        _wood.value = repository.getWood().value ?: 0

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

    fun load() = effect {
        reset()
        // Todo: Resolve initial load from disk without ticking, but update values according to repository values
        tick()
    }

    private fun effect(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }

    fun reset() = repository.reset()

    fun score(): Int {
        return (getTrees().value ?: 0) * 100 + (getWood().value ?: 0)
    }

    fun tick() {
        addWood()
    }
}