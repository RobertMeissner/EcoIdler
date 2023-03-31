package com.example.ecoidler.ui.navigation

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object Support : Screens("support")
    object NewGame : Screens("newGame")
    object Story : Screens("story/{id}")
    object Intro : Screens("intro")
    object Lost : Screens("lost")
}
