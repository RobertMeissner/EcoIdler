package com.example.ecoidler.data

class FakeDatabase private constructor() {

    var fakeDao = FakeDao()
        private set

    companion object {
        // volatile: all threads have the same data immediately
        @Volatile private var instance: FakeDatabase? = null

        // instantiate a Singleton
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FakeDatabase().also { instance = it}
            }
    }
}
