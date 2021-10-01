package xyz.tberghuis.mylists.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.data.Mylist
import xyz.tberghuis.mylists.data.MylistDao

import javax.inject.Inject

@HiltViewModel
class AddListViewModel @Inject constructor(
  private val mylistDao: MylistDao
) : ViewModel() {

  val mylistTextField = mutableStateOf("")

  fun addListName() {
    if (mylistTextField.value.isNotBlank()) {
      viewModelScope.launch {
        // trim as hack to remove \n character
        mylistDao.insertAll(Mylist(mylistText = mylistTextField.value.trim(), myitemDraftText = ""))
      }
    }
  }
}