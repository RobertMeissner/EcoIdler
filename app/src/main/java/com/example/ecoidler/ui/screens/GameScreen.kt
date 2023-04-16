package com.example.ecoidler.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ecoidler.ui.*
import java.util.*


data class MaterialStats(val name: String, val amount: Number)


@Composable
fun GameScreen(
    viewModel: DataViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Last tick at ${uiState.lastTick}")
        MaterialStats(
            name = "score",
            amount = viewModel.score()
        )
        Stats(materials = uiState.buildings)
        Stats(materials = uiState.materials)
        uiState.materials.forEach {
            MaterialPill(
                value = it,
                isAffordable = viewModel.isAffordable(it)
            )
        }
        uiState.buildings.forEach {
            MaterialPill(
                value = it,
                isAffordable = viewModel.isAffordable(it)
            )
        }
    }
}


@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
)
@Composable
fun MaterialPillPreview() {
    val costs = listOf(Cost(ValueName.WOOD, 10F), Cost(ValueName.COAL, 3F))
    Column {
        MaterialPill(Value(ValueName.WOOD, costs = costs))
        MaterialPill(Value(ValueName.WOOD, costs = costs), false)
    }
}

@Composable
private fun MaterialPill(
    value: IValue, isAffordable: Boolean = true
) {
    val costs = value.costs
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MinedMaterials(material = value)
        if (isAffordable)
            WorkerButton(
                icon = Icons.Default.Face,
                onClick = { value.increase() })
        Column {
            costs.forEach { Text("${it.name}:${it.amount}") }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
)
@Composable
fun GameScreenPreview() {
    val viewModel = DataViewModel()
    GameScreen(viewModel)
}

@Composable
fun WorkerButton(icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(5.dp)
    ) {
        Text("+1")
        Icon(icon, contentDescription = null)
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
)
@Composable
fun MaterialCounterPreview() {
    WorkerButton(icon = Icons.Default.AccountBox, onClick = {})
}

@Composable
fun Stats(materials: MutableList<IValue>) {
    LazyColumn {
        items(materials.size) {
            materials.forEach { material ->
                MaterialStat(
                    name = material.name.toString().lowercase(),
                    amount = material.amount,
                    incrementer = material.increment.toInt()
                )
            }
        }
    }
}


@Composable
fun MaterialStat(name: String, amount: Number, incrementer: Number) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (amount.toDouble() >= 0.0) {
            Text(text = "$name: $amount/$incrementer")
        }
    }
}

@Composable
fun MinedMaterials(material: IValue) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (material.amount >= 0.0) {
            Icon(Icons.Default.Info, contentDescription = null)
            Text(
                text = "${material.name}: ${material.amount}",
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
)
@Composable
fun DefaultPreview() {
    Surface {
        Column {
            val uiState = GameUiState(lastTick = Date())
            Stats(uiState.materials)
        }
    }

}