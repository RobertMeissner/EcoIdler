package com.example.ecoidler.ui.navigation

import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.ecoidler.ui.DataViewModel

@Composable
fun TopAppBarCompose(navController: NavHostController, viewModel: DataViewModel) {
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
                navController.navigate(Screens.Support.route)
            }) {
                Icon(Icons.Default.Favorite, contentDescription = null)
            }
            IconButton(onClick = {
                if (!viewModel.hasLost()) navController.navigate(Screens.Home.route)
                else navController.navigate(Screens.NewGame.route)
            }) {
                Icon(Icons.Default.Home, contentDescription = null)
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