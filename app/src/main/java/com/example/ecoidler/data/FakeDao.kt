package com.example.ecoidler.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FakeDao {

    // data model
    private var woodGatherer = 0
    private val woodGatherers = MutableLiveData<Int>()
    private var woodChopper = 0
    private val woodChoppers = MutableLiveData<Int>()

    private var woodData = 0
    private val wood = MutableLiveData<Int>()
    private var trees = 100
    private val liveTrees = MutableLiveData<Int>()

    init {
        wood.value = woodData
        liveTrees.value = trees
    }

    fun addWoodGatherer() {
        woodGatherer += 1
        woodGatherers.value = woodGatherer
    }

    fun addWoodChoppers() {
        woodChopper += 1
        woodChoppers.value = woodChopper
    }

    fun addWood(gatheredWood: Int) {
        woodData += gatheredWood
        wood.value = woodData
    }

    fun getWood() = wood as LiveData<Int>
    fun getWoodGatherers() = woodGatherers as LiveData<Int>
    fun getWoodChoppers() = woodChoppers as LiveData<Int>
    fun cutTree(number: Int) {
        trees -= number
        liveTrees.value = trees

    }

    fun getTrees() = liveTrees as LiveData<Int>

}