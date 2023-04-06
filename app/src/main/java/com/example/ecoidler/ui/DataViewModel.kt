package com.example.ecoidler.ui

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


data class GameUiState(
    val fuel: Int = 0,
    val woodWorker: Int = 0,
    val uraniumWorker: Int = 0,
    val trees: Int = 0,

    val materials: List<Material> = listOf()
)

interface IMaterial {
    var amount: Float
    var capacity: Float
    var planetCapacity: Float
    var name: String

    fun mine(timeDiff: Float, workers: Float): Float

}

sealed class Material(material_name: String) : IMaterial {
    override var amount: Float = 0.0f
    override var capacity: Float = 0.0f
    override var planetCapacity: Float = 0.0f
    final override var name: String = ""

    init {
        name = material_name
    }

    override fun mine(timeDiff: Float, workers: Float): Float {
        var mined = timeDiff * workers
        if (mined > (capacity - amount)) mined = capacity - amount
        planetCapacity -= mined
        return mined
    }

    object Wood : Material(material_name = "wood")
}

class DataViewModel() : ViewModel(), KoinComponent {
    private val _uiState = MutableStateFlow(GameUiState())
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Todo: Redundant for now but important for persisting
    private var repository: Repository = Repository.getInstance(FakeDatabase.getInstance().fakeDao)
    private fun tickStats() {
        val fuel = _uiState.value.woodWorker + 10 * _uiState.value.uraniumWorker
        // Todo connect _wood better with repository wood
        _uiState.update { state ->
            state.copy(
                fuel = _uiState.value.fuel + fuel,
                trees = _uiState.value.trees - _uiState.value.uraniumWorker
            )
        }
    }

    fun addWoodWorker() =
        _uiState.update { state -> state.copy(woodWorker = state.woodWorker + 1) }

    fun addWoodChoppers() =
        _uiState.update { state -> state.copy(uraniumWorker = state.uraniumWorker + 1) }

    fun lost() = uiState.value.trees <= 0

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
                fuel = 0,
                woodWorker = 0,
                uraniumWorker = 0,
                trees = 100
            )
        }
    }

    fun score(): Int {
        return _uiState.value.trees * 100 + _uiState.value.fuel
    }

    fun tick(navController: NavHostController) {
        tickStats()
        if (lost()) navController.navigate(Screens.Lost.route)
    }
}