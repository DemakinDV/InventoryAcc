package com.example.inventoryacc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.inventoryacc.ui.screens.*
import com.example.inventoryacc.ui.viewmodels.InventoryViewModel

sealed class Screen(val route: String) {
    object MainMenu : Screen("main_menu")
    object Login : Screen("login")
    object InventoryList : Screen("inventory_list")
    object AddItem : Screen("add_item")
    object EditItem : Screen("edit_item/{itemId}") {
        fun passId(itemId: String): String = "edit_item/$itemId"
    }
    object ItemDetail : Screen("item_detail/{itemId}") {
        fun passId(itemId: String): String = "item_detail/$itemId"
    }
}

@Composable
fun NavGraph(viewModel: InventoryViewModel) {
    val navController = rememberNavController()
    var isAuthenticated by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) Screen.InventoryList.route else Screen.MainMenu.route
    ) {
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    isAuthenticated = true
                    navController.navigate(Screen.InventoryList.route) {
                        popUpTo(Screen.MainMenu.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.InventoryList.route) {
            InventoryListScreen(
                onNavigateToAdd = {
                    navController.navigate(Screen.AddItem.route)
                },
                onNavigateToDetail = { itemId ->
                    navController.navigate(Screen.ItemDetail.passId(itemId))
                },
                viewModel = viewModel
            )
        }


        composable(Screen.AddItem.route) {
            AddEditItemScreen(
                itemId = null,
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.EditItem.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            AddEditItemScreen(
                itemId = itemId,
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
            ItemDetailScreen(
                itemId = itemId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.EditItem.passId(id))
                },
                viewModel = viewModel
            )
        }
    }
}