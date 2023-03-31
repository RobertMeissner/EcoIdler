package com.example.ecoidler

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecoidler.ui.DataViewModel
import com.example.ecoidler.ui.navigation.Screens
import com.example.ecoidler.ui.navigation.TopAppBarCompose
import com.example.ecoidler.ui.screens.GameScreen
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
fun Greeting(name: String) {
    Text(
        text = "Hello $name!", textAlign = TextAlign.Center
    )
}
