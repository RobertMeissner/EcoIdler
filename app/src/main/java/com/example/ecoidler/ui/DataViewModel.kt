package com.example.ecoidler.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.ecoidler.data.FakeDatabase
import com.example.ecoidler.data.Repository
import com.example.ecoidler.ui.navigation.Screens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.util.*


data class GameUiState(
    var lastTick: Date,
    var materials: SnapshotStateList<IValue> = mutableStateListOf()

)

interface IValue {
    fun addWorker()
    fun mine()

    var amount: Float
    var workers: Float
    var name: String
}

open class Value(valueName: String) : IValue {
    override var amount: Float = 0F
    override var workers: Float = 0F
    final override var name: String = ""

    init {
        name = valueName
    }


    override fun addWorker() {
        workers += 1
        println("${name}: $workers")
    }

    override fun mine() {
        val timeDiff = 1F
        val mined = timeDiff * workers
        amount += mined
        println("${name}: $amount by $workers")
    }

    object Wood : Value("wood")
    object Coal : Value("coal")
}


class DataViewModel() : ViewModel(), KoinComponent {
    private val _uiState = MutableStateFlow(GameUiState(lastTick = Date()))
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()


    // Todo: Redundant for now but important for persisting
    private var repository: Repository = Repository.getInstance(FakeDatabase.getInstance().fakeDao)

    private fun tickStats() {
        _uiState.value.materials.forEach { material -> material.mine() }

        _uiState.update { state ->
            state.copy(
                lastTick = Date()
            )
        }
    }


    fun lost() = uiState.value.materials[0].amount >= 10

    fun load(navController: NavHostController) = effect {
        reset()
        // Todo: Resolve initial load from disk without ticking, but update values according to repository values
        tick(navController)
    }

    private fun effect(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }

    private fun reset() {
        _uiState.update { state ->
            state.copy(
                materials = mutableStateListOf(Value.Wood, Value.Coal)
            )
        }
    }

    fun score(): Int {
        return _uiState.value.materials[0].amount.toInt()
    }

    fun tick(navController: NavHostController) {
        tickStats()
        if (lost()) navController.navigate(Screens.Lost.route)
    }
}