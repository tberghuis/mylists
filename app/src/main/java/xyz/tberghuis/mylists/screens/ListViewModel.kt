package xyz.tberghuis.mylists.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.data.Myitem
import xyz.tberghuis.mylists.data.MyitemDao
import xyz.tberghuis.mylists.data.MylistDao

import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import xyz.tberghuis.mylists.util.logd


// todo hilt inject mylistId????
// would i be better off without hilt???
@HiltViewModel
class ListViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val myitemDao: MyitemDao,
  private val mylistDao: MylistDao
) : ViewModel() {

  // this by operator/delegate makes MutableState clever somehow
  var confirmDeleteMyitemDialog by mutableStateOf<Myitem?>(null)

  // using nullable type to store state is probably anti-pattern
//  var confirmDeleteMylistDialog by mutableStateOf<Int?>(null)
  var confirmDeleteMylistDialog by mutableStateOf<Boolean>(false)

  //
  var editMylistTitleDialog by mutableStateOf<String?>(null)
  // todo refactor mylist_text to mylist_title

  var editMyitemDialog by mutableStateOf<Myitem?>(null)

  val mylistId = savedStateHandle.get<Int>("mylistId")!!
  val draftTextFieldStateFlow = MutableStateFlow<String?>(null)

  init {
    viewModelScope.launch {
      draftTextFieldStateFlow.value = mylistDao.myitemDraftTextFlow(mylistId).map {
        it ?: ""
      }.first()
    }
  }

  fun updateMylistDraftText(draftText: String) {
    viewModelScope.launch {

      draftTextFieldStateFlow.value = draftText
      mylistDao.updateMyitemDraftText(mylistId = mylistId, myitemDraftText = draftText)
    }
  }

  fun addListItem(itemText: String) {
    if (itemText.isNotBlank()) {
      viewModelScope.launch(Dispatchers.IO) {
        // todo test null
        val maxOrder = myitemDao.getMaxOrder(mylistId) ?: -1
        myitemDao.insertAll(
          Myitem(
            mylistId = mylistId, myitemText = itemText.trim(), myitemOrder = maxOrder + 1
          )
        )
        mylistDao.updateMyitemDraftText(mylistId = mylistId, myitemDraftText = "")
        draftTextFieldStateFlow.value = ""
      }
    }
  }

  fun getAppBarTitle(): LiveData<String> {
    return mylistDao.getMylistText(mylistId)
  }

  fun updateMylistTitle(mylistTitle: String) {
    // todo prevent blank mylistTitle
    viewModelScope.launch {
      mylistDao.updateMylistText(mylistTitle, mylistId)
    }
  }

  fun updateMyitemText(myitemText: String, myitemId: Int) {
    viewModelScope.launch {
      myitemDao.updateMyitemText(myitemText, myitemId)
    }
  }

  fun getAllMyitems(): Flow<List<Myitem>> {
    return myitemDao.getAll(mylistId)
  }

  fun deleteMyitem() {
    viewModelScope.launch {
      val myitem = confirmDeleteMyitemDialog!!
      // decrement where order is >
      // should i do in transaction???
      myitemDao.decrementOrder(myitem.mylistId, myitem.myitemOrder)
      myitemDao.delete(myitem)
      confirmDeleteMyitemDialog = null
    }
  }

  fun deleteMylist() {
    viewModelScope.launch {
      mylistDao.delete(mylistId)
      confirmDeleteMylistDialog = false
    }
  }

  // doitwrong
  fun update(vararg myitems: Myitem) {
    viewModelScope.launch {
      myitemDao.update(*myitems)
    }
  }

}