package com.example.ecoidler

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecoidler.ui.DataViewModel
import com.example.ecoidler.ui.theme.EcoIdlerTheme

@Composable
fun EcoIdler(viewModel: DataViewModel) {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopAppBarCompose(navController) },
//            bottomBar = { BottomBar(navController)            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") {
                    Column {

                        Greeting("EcoIdler")
                        val initialStats =
                            listOf<MaterialStats>(
                                MaterialStats(
                                    name = "wood",
                                    amount = viewModel.getWood().value ?: 0
                                )
                            )
                        Stats(stats = initialStats)
                        MaterialCounter(material_name = "Wood", onClick = { })
                        MaterialCounter(material_name = "Stone", onClick = { })
                    }
                }
                composable("support") {
                    Column {

                        Greeting("you")
                        Text("Thank you for wanting to support me.")
                    }
                }
            }
        }
    }


}


@Composable
fun TopAppBarCompose(navController: NavHostController) {
    val context = LocalContext.current
    val showMenu = remember {
        mutableStateOf(false)
    }
    TopAppBar(
        contentColor = MaterialTheme.colors.secondaryVariant,
        title = { Text(text = "Top app bar") },
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(context, "TopAppBar menu clicked", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = {
                makeToast(context, "Support")
                navController.navigate("support")
            }) {
                Icon(Icons.Default.Favorite, contentDescription = null)
            }
            IconButton(onClick = {
                makeToast(context, "Search")
                navController.navigate("home")
            }) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
            IconButton(onClick = { showMenu.value = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
            DropdownMenu(expanded = showMenu.value, onDismissRequest = { showMenu.value = false }) {
                DropdownMenuItem(onClick = {
                    Toast.makeText(
                        context,
                        "Settings clicked",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text(text = "Settings")
                }
                DropdownMenuItem(onClick = {
                    Toast.makeText(
                        context,
                        "Logout clicked",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text(text = "Logout")
                }
            }
        })

}

fun makeToast(ctx: Context, msg: String) {
    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
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
        if (amount.toDouble() >= 0.0) {
            Text(text = "$name mined:")
            Text(text = amount.toString(), color = MaterialTheme.colors.secondaryVariant)
            Text(text = "$name remaining:")
            Text(
                text = (100.0 - amount.toDouble()).toString(),
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