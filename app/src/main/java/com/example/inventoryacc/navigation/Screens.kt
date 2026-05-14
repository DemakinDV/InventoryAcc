package com.example.inventoryacc.navigation

sealed class Screens(val route: String) {
    object MainMenu : Screens("main_menu")
    object Login : Screens("login")
}
