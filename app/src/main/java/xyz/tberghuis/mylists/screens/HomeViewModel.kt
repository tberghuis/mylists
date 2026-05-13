package xyz.tberghuis.mylists.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import xyz.tberghuis.mylists.data.Mylist
import xyz.tberghuis.mylists.data.MylistDao


class HomeViewModel constructor(
  private val mylistDao: MylistDao
) : ViewModel() {
  fun getAllListNames(): Flow<List<Mylist>> {
    return mylistDao.getAll()
  }
  fun getCount(mylistId: Int): LiveData<Int> {
    return mylistDao.getMyitemCount(mylistId)
  }
}