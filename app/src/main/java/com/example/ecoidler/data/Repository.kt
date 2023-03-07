package com.example.ecoidler.data

import androidx.lifecycle.LiveData

class Repository private constructor(private val dao: FakeDao) {


    fun getWoodGatherers(): LiveData<Int> = dao.getWoodGatherers()
    fun getWoodChoppers(): LiveData<Int> = dao.getWoodChoppers()

    fun getWood() = dao.getWood()
    fun addWoodGatherer() = dao.addWoodGatherer()
    fun addWoodChoppers() = dao.addWoodChoppers()
    fun addWood(wood: Int) = dao.addWood(wood)
    fun cutTree(number: Int) = dao.cutTree(number)
    fun getTrees() = dao.getTrees()

    fun reset() = dao.reset()

    companion object {
        // volatile: all threads have the same data immediately
        @Volatile
        private var instance: Repository? = null

        // instantiate a Singleton
        fun getInstance(dao: FakeDao) =
            instance ?: synchronized(this) {
                instance ?: Repository(dao).also { instance = it }
            }
    }
}