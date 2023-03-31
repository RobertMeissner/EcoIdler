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
    val wood: Int = 0,
    val woodGatherers: Int = 0,
    val woodChoppers: Int = 0,
    val trees: Int = 0,
)

class DataViewModel() : ViewModel(), KoinComponent {
    private val _uiState = MutableStateFlow(GameUiState())
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Todo: Redundant for now but important for persisting
    private var repository: Repository = Repository.getInstance(FakeDatabase.getInstance().fakeDao)
    private fun tickStats() {
        val choppedWood = _uiState.value.woodGatherers + 10 * _uiState.value.woodChoppers
        // Todo connect _wood better with repository wood
        _uiState.update { state ->
            state.copy(
                wood = _uiState.value.wood + choppedWood,
                trees = _uiState.value.trees - _uiState.value.woodChoppers
            )
        }
    }

    fun addWoodGatherer() =
        _uiState.update { state -> state.copy(woodGatherers = state.woodGatherers + 1) }

    fun addWoodChoppers() =
        _uiState.update { state -> state.copy(woodChoppers = state.woodChoppers + 1) }

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
                wood = 0,
                woodGatherers = 0,
                woodChoppers = 0,
                trees = 100
            )
        }
    }

    fun score(): Int {
        return _uiState.value.trees * 100 + _uiState.value.wood
    }

    fun tick(navController: NavHostController) {
        tickStats()
        if (lost()) navController.navigate(Screens.Lost.route)
    }
}