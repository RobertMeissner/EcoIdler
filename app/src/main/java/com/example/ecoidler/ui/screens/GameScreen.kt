package com.example.ecoidler.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ecoidler.ui.DataViewModel
import com.example.ecoidler.ui.GameUiState


data class MaterialStats(val name: String, val amount: Number)

enum class Materials {
    WOOD, STONE, FOOD, WATER
}

@Composable
fun GameScreen(
    viewModel: DataViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value
    Column(modifier = Modifier.fillMaxWidth()) {
        MaterialStats(
            name = "score",
            amount = viewModel.score()
        )
        Stats(uiState)
        MaterialPill(viewModel, Materials.WOOD)
        MaterialPill(viewModel, Materials.STONE)
        MaterialPill(viewModel, Materials.FOOD)
        MaterialPill(viewModel, Materials.WATER)
    }
}


@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
)
@Composable
fun MaterialPillPreview() {
    val viewModel = DataViewModel()
    MaterialPill(viewModel, Materials.STONE)
}

@Composable
private fun MaterialPill(
    viewModel: DataViewModel,
    material: Materials
) {
    val uiState = viewModel.uiState.collectAsState().value
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MinedMaterials(name = material.name, uiState)
        WorkerButton(
            icon = Icons.Default.Info,
            onClick = { viewModel.addWoodGatherer() })
        WorkerButton(
            icon = Icons.Default.Face,
            onClick = { viewModel.addWoodChoppers() })
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
fun Stats(uiState: GameUiState) {
    val stats =
        listOf(
            MaterialStats(
                name = "wood",
                amount = uiState.wood
            ),
            MaterialStats(
                name = "Gatherers",
                amount = uiState.woodGatherers
            ),
            MaterialStats(
                name = "Choppers",
                amount = uiState.woodChoppers
            )
        )
    LazyColumn {
        items(stats) { stat ->
            MaterialStat(name = stat.name, amount = stat.amount)
        }
    }
}


@Composable
fun MaterialStat(name: String, amount: Number) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (amount.toDouble() >= 0.0) {
            Text(text = "$name: $amount")
        }
    }
}

@Composable
fun MinedMaterials(name: String, uiState: GameUiState) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (uiState.wood.toDouble() >= 0.0) {
            Icon(Icons.Default.Info, contentDescription = null)
            Text(
                text = "${name}:",
                color = MaterialTheme.colors.primaryVariant
            )
            Text(
                text = uiState.trees.toString(),
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
            val uiState = GameUiState(wood = 3)
            Stats(uiState)
        }
    }

}