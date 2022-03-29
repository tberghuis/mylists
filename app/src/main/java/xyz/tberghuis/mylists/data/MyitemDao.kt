package xyz.tberghuis.mylists.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MyitemDao {

//  @Query("SELECT * FROM myitem WHERE mylist_id = :mylistId ORDER BY myitem_order")
//  fun getAll(mylistId: Int): Flow<List<Myitem>>
  @Query("SELECT * FROM myitem WHERE mylist_id = :mylistId ORDER BY myitem_id DESC")
  fun getAll(mylistId: Int): Flow<List<Myitem>>

  @Insert
  suspend fun insertAll(vararg myitem: Myitem)

//  @Query("SELECT max(myitem_order) FROM myitem where mylist_id = :mylistId")
//  fun getMaxOrder(mylistId: Int): Int?

  @Delete
  suspend fun delete(vararg myitem: Myitem)

  @Query("UPDATE myitem set myitem_text = :myitemText WHERE myitem_id = :myitemId")
  suspend fun updateMyitemText(myitemText: String, myitemId: Int)
}