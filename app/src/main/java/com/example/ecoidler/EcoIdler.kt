package com.example.ecoidler

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ecoidler.ui.theme.EcoIdlerTheme

@Composable
fun EcoIdler() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {

            Greeting("EcoIdler")
            val materials = listOf<MaterialStats>(MaterialStats(name = "wood", amount = 10))
            Stats(stats = materials)
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun Stats(stats: List<MaterialStats>) {
    print(stats)
    LazyColumn {
        items(stats) { stat ->
            MaterialStat(name = stat.name, amount = stat.amount)
        }
    }
}


data class MaterialStats(val name: String, val amount: Number)


@Composable
fun MaterialStat(name: String, amount: Number) {
    Column {
        Text(text = "$name mined:")
        Text(text = amount.toString(), color = MaterialTheme.colors.secondaryVariant)
        Text(text = "$name remaining:")
        Text(
            text = (100.0 - amount.toDouble()).toString(),
            color = MaterialTheme.colors.primaryVariant
        )
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun DefaultPreview() {
    EcoIdlerTheme {
        Surface {
            Column() {

                Greeting("Android")
                val materials = listOf<MaterialStats>(
                    MaterialStats(name = "wood", amount = 3),
                    MaterialStats(name = "stone", amount = 30)
                )
                Stats(stats = materials)
            }
        }

    }
}