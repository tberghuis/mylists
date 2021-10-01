package xyz.tberghuis.mylists.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import xyz.tberghuis.mylists.data.Mylist
import xyz.tberghuis.mylists.data.MylistDao

import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val mylistDao: MylistDao
) : ViewModel() {
  fun getAllListNames(): Flow<List<Mylist>> {
    return mylistDao.getAll()
  }
  fun getCount(mylistId: Int): LiveData<Int> {
    return mylistDao.getMyitemCount(mylistId)
  }
}