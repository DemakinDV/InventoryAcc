package com.example.inventoryacc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventoryacc.ui.screens.LoginScreen
import com.example.inventoryacc.ui.screens.MainMenuScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.MainMenu.route
    ) {
        composable(route = Screens.MainMenu.route) {
            MainMenuScreen(
                onLoginClick = {
                    navController.navigate(Screens.Login.route)
                }
            )
        }
        composable(route = Screens.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Здесь потом будет переход в меню управления запасами после авторизации
                    navController.popBackStack()
                }
            )
        }
    }
}
