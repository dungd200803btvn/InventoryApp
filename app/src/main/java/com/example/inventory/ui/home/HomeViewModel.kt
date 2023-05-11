
package com.example.inventory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import com.example.inventory.data.OfflineItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View Model to retrieve all items in the Room database.
 */
class HomeViewModel(itemsRepository: ItemsRepository) : ViewModel() {
    // map de chuyen tu Item sang Homeuistate ,statein chuyen flow thanh stateflow
    val homeUiState : StateFlow<HomeUiState> = itemsRepository.getAllItemsStream().map { HomeUiState(it) }.stateIn(scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(HomeViewModel.Companion.TIMEOUT_MILLIS) ,
    initialValue = HomeUiState())
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<Item> = listOf())
