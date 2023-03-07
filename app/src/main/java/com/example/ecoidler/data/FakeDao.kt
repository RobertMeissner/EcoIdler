package com.example.ecoidler.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FakeDao {

    // data model
    private var woodGatherer = 0
    private val woodGatherers = MutableLiveData<Int>()
    private var woodData = 0
    private val wood = MutableLiveData<Int>()

    init {
        wood.value = woodData
    }

    fun addWoodGatherer() {
        woodGatherer += 1
        woodGatherers.value = woodGatherer
    }
    fun addWood(gatheredWood: Int) {
        woodData += gatheredWood
        wood.value = woodData
    }

    fun getWood() = wood as LiveData<Int>
    fun getWoodGatherers() = woodGatherers as LiveData<Int>

}