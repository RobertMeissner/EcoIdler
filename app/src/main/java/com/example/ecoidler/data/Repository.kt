package com.example.ecoidler.data

import androidx.lifecycle.LiveData

class Repository private constructor(private val dao: FakeDao) {


    fun getWoodGatherers(): LiveData<Int> = dao.getWoodGatherers()

    fun getWood() = dao.getWood()
    fun addWoodGatherer() = dao.addWoodGatherer()
    fun addWood(wood: Int) = dao.addWood(wood)

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