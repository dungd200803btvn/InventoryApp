
package com.example.inventory.ui.item
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an item from the data source.
 */
class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])
    val uistate : StateFlow<ItemUiState> = itemsRepository.getItemStream(itemId).filterNotNull()
        .map { it.toItemUiState(actionEnabled =  it.quantity >0) }
        .stateIn( scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ItemUiState())
        fun reduceQuantitybyone(){
            viewModelScope.launch {
                val currentItem = uistate.value.toItem()
                if(currentItem.quantity>0){
                    itemsRepository.updateItem(currentItem.copy(quantity = currentItem.quantity-1))
                }
            }
        }
        suspend fun deleteItem(){
            itemsRepository.deleteItem(uistate.value.toItem())
        }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
