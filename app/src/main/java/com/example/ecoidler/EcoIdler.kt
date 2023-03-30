package com.example.ecoidler

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecoidler.ui.DataViewModel
import com.example.ecoidler.ui.GameUiState
import com.example.ecoidler.ui.navigation.TopAppBarCompose
import com.example.ecoidler.ui.theme.EcoIdlerTheme
import kotlinx.coroutines.delay

@Composable
fun EcoIdler(viewModel: DataViewModel) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    viewModel.load(navController)
    val uiState by viewModel.uiState.collectAsState()

    StartLoop(viewModel, navController)
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopAppBarCompose(navController, viewModel = viewModel) },
//            bottomBar = { BottomBar(navController)            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "newGame",
                modifier = Modifier.padding(innerPadding)
            ) {

                composable("home") {
                    Column {

                        Greeting("EcoIdler")
                        val initialStats =
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
                        Stats(stats = initialStats)
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
                composable("support") {
                    Column {
                        Greeting("you")
                        Text("Thank you for wanting to support me.")
                    }
                }
                composable("newGame") {
                    Column {
                        Greeting("you")
                        Button(onClick = {
                            viewModel.load(navController)
                            navController.navigate("home")
                        }) {

                            Text("New Game.")
                        }
                    }
                }
                composable("lost") {
                    Column {

                        Text("You have lost.")
                    }
                }
            }
        }
    }


}

@Composable
private fun StartLoop(
    viewModel: DataViewModel,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        while (true) {
            if (!viewModel.lost()) viewModel.tick(navController)
            delay(1000)
        }
    }
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
fun Greeting(name: String) {
    Text(
        text = "Hello $name!", textAlign = TextAlign.Center
    )
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
                text = (uiState.trees - uiState.wood.toDouble()).toString(),
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
    EcoIdlerTheme {
        Surface {
            Column {

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