package com.example.inventoryacc.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryacc.domain.model.InventoryItem
import com.example.inventoryacc.domain.usecase.ItemUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val itemUseCases: ItemUseCases
) : ViewModel() {

    val allItems = itemUseCases.getAllItems().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getItemById(id: String): StateFlow<InventoryItem?> {
        val flow = MutableStateFlow<InventoryItem?>(null)
        viewModelScope.launch {
            val item = itemUseCases.getItemById(id)
            flow.value = item
        }
        return flow.asStateFlow()
    }

    fun addItem(item: InventoryItem) {
        viewModelScope.launch {
            itemUseCases.addItem(item)
        }
    }

    fun updateItem(item: InventoryItem) {
        viewModelScope.launch {
            itemUseCases.updateItem(item)
        }
    }

    fun deleteItem(item: InventoryItem) {
        viewModelScope.launch {
            itemUseCases.deleteItem(item)
        }
    }
}
