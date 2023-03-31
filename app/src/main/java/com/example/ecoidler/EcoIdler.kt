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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecoidler.ui.DataViewModel
import com.example.ecoidler.ui.GameUiState
import com.example.ecoidler.ui.navigation.Screens
import com.example.ecoidler.ui.navigation.TopAppBarCompose
import com.example.ecoidler.ui.theme.EcoIdlerTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EcoIdler() {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val viewModel = getViewModel<DataViewModel> { parametersOf() }
    viewModel.load(navController)

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
                composable(Screens.Home.route) {
                    GameScreen(viewModel)
                }
                composable(Screens.Support.route) {
                    Column {
                        Greeting("you")
                        Text(stringResource(R.string.support_me))
                    }
                }
                composable(Screens.NewGame.route) {
                    NewGameScreen(viewModel, navController)
                }
                composable(Screens.Story.route) {
                    val storyId = it.arguments?.getInt("id") ?: R.string.intro
                    StoryScreen(viewModel, navController, storyId)
                }
                composable(Screens.Intro.route) {
                    StoryScreen(viewModel, navController, R.string.intro)
                }
                composable(Screens.Lost.route) {
                    LostScreen(viewModel, navController)
                }
            }
        }
    }


}

@Composable
private fun StoryScreen(
    viewModel: DataViewModel,
    navController: NavHostController,
    story_id: Int,
) {
    Column {
        Greeting(stringResource(R.string.ai_name))
        Text(stringResource(story_id))
        Button(onClick = {
            viewModel.load(navController)
            navController.navigate(Screens.Home.route)
        }) {
            Text("Begin.")
        }
    }
}

@Composable
private fun LostScreen(
    viewModel: DataViewModel,
    navController: NavHostController
) {
    Column {
        Text("You have lost.")
        Text("You have made ${viewModel.score()} points.")
        NewGameScreen(viewModel, navController)
    }
}

@Composable
private fun GameScreen(
    viewModel: DataViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value
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

//
//@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
//)
//@Composable
//fun GameScreenPreview() {
//    val viewModel = DataViewModel()
//    EcoIdlerTheme {
//        GameScreen (viewModel)
//
//    }
//}

@Composable
private fun NewGameScreen(
    viewModel: DataViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier

) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.new_game)) },
        modifier = modifier,
        dismissButton = {},
        confirmButton = {
            TextButton(onClick = {
                viewModel.load(navController)
                navController.navigate(Screens.Intro.route)
            }) {
                Text("New Game")
            }
        })
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
    EcoIdlerTheme {
        Surface {
            Column {

                Greeting("Android")
                val materials = listOf(
                    MaterialStats(name = "wood", amount = 3),
                    MaterialStats(name = "stone", amount = 30)
                )
                Stats(stats = materials)
            }
        }

    }
}