package com.example.ecoidler.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.ecoidler.ui.DataViewModel
import com.example.ecoidler.ui.GameUiState


data class MaterialStats(val name: String, val amount: Number)

@Composable
fun GameScreen(
    viewModel: DataViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value
    Column {
        Stats(uiState)
        MinedMaterials(name = "wood", uiState)
        MaterialCounter(
            material_name = "Wood",
            onClick = { viewModel.addWoodGatherer() })
        MaterialCounter(
            material_name = "Wood Choppers",
            onClick = { viewModel.addWoodChoppers() })
        MaterialCounter(material_name = "Stone", onClick = { })
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
fun MaterialCounter(material_name: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Add $material_name gatherer")
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
)
@Composable
fun MaterialCounterPreview() {
    MaterialCounter(material_name = "Obsidian", onClick = {})
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.wood.toDouble() >= 0.0) {
            Text(text = "$name remaining:")
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