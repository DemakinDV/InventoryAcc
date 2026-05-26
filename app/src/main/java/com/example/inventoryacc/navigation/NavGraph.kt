package com.example.inventoryacc.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.inventoryacc.data.local.InventoryDatabase
import com.example.inventoryacc.ui.screens.*
import com.example.inventoryacc.ui.viewmodels.AccountViewModel
import com.example.inventoryacc.ui.viewmodels.InventoryViewModel
import kotlinx.coroutines.runBlocking

sealed class Screen(val route: String) {
    object MainMenu : Screen("main_menu")
    object Login : Screen("login")
    object Register : Screen("register")
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
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = InventoryDatabase.getDatabase(context)
    
    val accountViewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(database)
    )
    
    var isAuthenticated by remember { mutableStateOf(false) }
    var currentLogin by remember { mutableStateOf("") }
    var currentAccountId by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        val loggedIn = runBlocking { accountViewModel.getLoggedInAccount() }
        if (loggedIn != null) {
            currentLogin = loggedIn.login
            currentAccountId = loggedIn.id
            isAuthenticated = true
        }
    }
    
    val inventoryViewModel: InventoryViewModel = viewModel(
        factory = InventoryViewModelFactory(database, currentAccountId)
    )
    
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
                    val loggedIn = runBlocking { accountViewModel.getLoggedInAccount() }
                    if (loggedIn != null) {
                        currentLogin = loggedIn.login
                        currentAccountId = loggedIn.id
                        isAuthenticated = true
                        navController.navigate(Screen.InventoryList.route) {
                            popUpTo(Screen.MainMenu.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                viewModel = accountViewModel
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    val loggedIn = runBlocking { accountViewModel.getLoggedInAccount() }
                    if (loggedIn != null) {
                        currentLogin = loggedIn.login
                        currentAccountId = loggedIn.id
                        isAuthenticated = true
                        navController.navigate(Screen.InventoryList.route) {
                            popUpTo(Screen.MainMenu.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() },
                viewModel = accountViewModel
            )
        }
        
        composable(Screen.InventoryList.route) {
            val items by inventoryViewModel.allItems.collectAsState(initial = emptyList())
            InventoryListScreen(
                onNavigateToAdd = { navController.navigate(Screen.AddItem.route) },
                onNavigateToDetail = { itemId ->
                    navController.navigate(Screen.ItemDetail.passId(itemId))
                },
                onLogout = {
                    accountViewModel.logout {
                        isAuthenticated = false
                        navController.navigate(Screen.MainMenu.route) {
                            popUpTo(Screen.InventoryList.route) { inclusive = true }
                        }
                    }
                },
                currentLogin = currentLogin,
                items = items.filter { it.accountId == currentAccountId }
            )
        }
        
        composable(Screen.AddItem.route) {
            AddEditItemScreen(
                itemId = null,
                onNavigateBack = { navController.popBackStack() },
                viewModel = inventoryViewModel,
                accountId = currentAccountId
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
                viewModel = inventoryViewModel,
                accountId = currentAccountId
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
                viewModel = inventoryViewModel
            )
        }
    }
}

class AccountViewModelFactory(private val database: InventoryDatabase) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class InventoryViewModelFactory(
    private val database: InventoryDatabase,
    private val accountId: String
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            return InventoryViewModel(database, accountId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}