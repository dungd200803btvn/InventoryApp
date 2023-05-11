
package com.example.inventory.ui.item
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update an item from the [ItemsRepository]'s data source.
 */
class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])
    fun updateUiState(newUiState: ItemUiState){
        itemUiState = newUiState.copy(actionEnabled = newUiState.isValid())
    }
    suspend fun updateItem(){
        if(itemUiState.isValid()){
            itemsRepository.updateItem(itemUiState.toItem())
        }
    }
    init {
        viewModelScope.launch {
        itemUiState = itemsRepository.getItemStream(itemId).filterNotNull().first().toItemUiState(actionEnabled = true)
        }
    }
}
