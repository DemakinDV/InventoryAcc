package com.example.inventoryacc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.inventoryacc.data.local.InventoryDatabase
import com.example.inventoryacc.data.repository.ItemRepositoryImpl
import com.example.inventoryacc.domain.usecase.ItemUseCases
import com.example.inventoryacc.navigation.NavGraph
import com.example.inventoryacc.ui.theme.InventoryAccTheme
import com.example.inventoryacc.ui.viewmodels.InventoryViewModel

class MainActivity : ComponentActivity() {

    private lateinit var inventoryViewModel: InventoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = InventoryDatabase.getDatabase(this)
        val repository = ItemRepositoryImpl(database.itemDao())
        val itemUseCases = ItemUseCases(repository)
        inventoryViewModel = InventoryViewModel(itemUseCases)

        enableEdgeToEdge()
        setContent {
            InventoryAccTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InventoryApp()
                }
            }
        }
    }

    @Composable
    fun InventoryApp() {
        NavGraph(viewModel = inventoryViewModel)
    }
}
