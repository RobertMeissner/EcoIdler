package com.example.ecoidler.ui.navigation

import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

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
                navController.navigate("support")
            }) {
                Icon(Icons.Default.Favorite, contentDescription = null)
            }
            IconButton(onClick = {
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