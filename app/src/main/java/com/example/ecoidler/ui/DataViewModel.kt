package com.example.ecoidler.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.util.*


data class GameUiState(
    var lastTick: Date,
    var values: SnapshotStateList<IValue> = mutableStateListOf(),
    var lost: Boolean = false

)

interface IValue {
    fun increase()
    fun tick()

    var amount: Float
    var increment: Float
    var meta: ValueMeta
    var costs: List<Cost>

}

enum class ValueType{
    MATERIAL,
    BUILDING
}

sealed class ValueMeta(val name: String,val type: ValueType) {
    object WOOD: ValueMeta("wood", type=ValueType.MATERIAL)
    object COAL: ValueMeta("coal", ValueType.MATERIAL)
    object HOUSE: ValueMeta ("house", ValueType.BUILDING)
    object HEATER: ValueMeta ("heater", ValueType.BUILDING)

    override fun toString() = this.name
}

data class Cost(
    var name: ValueMeta,
    var amount: Float
)


open class Value(valueName: ValueMeta, override var costs: List<Cost> = listOf()) : IValue {
    override var amount: Float = 0F
    override var increment: Float = 0F
    final override var meta: ValueMeta = valueName

    override fun increase() {
        increment += 1
    }

    override fun tick() {
        val timeDiff = 1F
        val mined = timeDiff * increment
        amount += mined
    }

    object Wood : Value(ValueMeta.WOOD)
    object Coal : Value(ValueMeta.COAL)
    object House : Value(ValueMeta.HOUSE, listOf(Cost(ValueMeta.WOOD, 3F)))
    object Heater :
        Value(ValueMeta.HEATER, listOf(Cost(ValueMeta.WOOD, 3F), Cost(ValueMeta.COAL, 3F)))
}


class DataViewModel() : ViewModel(), KoinComponent {
    private val _uiState = MutableStateFlow(GameUiState(lastTick = Date()))
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()


    // Todo: Redundant for now but important for persisting
//    private var repository: Repository = Repository.getInstance(FakeDatabase.getInstance().fakeDao)

    private fun tickStats() {
        _uiState.value.values.forEach { it.tick() }

        _uiState.update { state ->
            state.copy(
                lastTick = Date()
            )
        }
    }


    fun hasLost(): Boolean {
        _uiState.update { state ->
            state.copy(
                // TODO: Add logic
                lost = uiState.value.values[0].amount >= 100
            )
        }
        return uiState.value.lost
    }

    fun load() {
        reset()
        // Todo: Resolve initial load from disk without ticking, but update values according to repository values
        tick()
    }

    private fun effect(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }

    private fun reset() {
        _uiState.update { state ->
            state.copy(
                values = mutableStateListOf(Value.Wood, Value.Coal, Value.House, Value.Heater),
                lost = false
            )
        }
    }

    fun score(): Int {
        // TODO: Add logic
        return _uiState.value.values[0].amount.toInt()
    }

    fun tick() {
        tickStats()
    }

    private fun filterByName(list: List<IValue>, name: String): List<IValue> {
        return list.filter { it.meta.name == name }
    }

    fun isAffordable(value: IValue): Boolean {
        if (value.costs.isEmpty()) return true

        for (cost in value.costs) {

            val neededMaterials =
                filterByName(_uiState.value.values, cost.name.name)
            if (neededMaterials.isEmpty())
            // yet unknown costs
                return false

            for (material in neededMaterials) {
                if (material.amount < cost.amount) {
                    return false
                }
            }
        }

        return true
    }


}