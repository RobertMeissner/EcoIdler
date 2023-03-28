package com.example.ecoidler

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecoidler.ui.theme.EcoIdlerTheme

@Composable
fun EcoIdler() {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopAppBarCompose() },
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
                        val materials =
                            listOf<MaterialStats>(MaterialStats(name = "wood", amount = 10))
                        Stats(stats = materials)
                        MaterialCounter(material_name = "Wood", onClick = { })
                        MaterialCounter(material_name = "Stone", onClick = { })
                    }
                }
            }
        }
    }


}


@Composable
fun TopAppBarCompose() {
    val context = LocalContext.current
    val showMenu = remember {
        mutableStateOf(false)
    }
    TopAppBar(
        contentColor = MaterialTheme.colors.primaryVariant,
        title = { Text(text = "Top app bar") },
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(context, "TopAppBar menu clicked", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        })
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