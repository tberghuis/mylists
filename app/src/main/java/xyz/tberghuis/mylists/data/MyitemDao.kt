package xyz.tberghuis.mylists.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MyitemDao {

  @Query("SELECT * FROM myitem WHERE mylist_id = :mylistId ORDER BY myitem_order")
  fun getAll(mylistId: Int): Flow<List<Myitem>>
//  @Query("SELECT * FROM myitem WHERE mylist_id = :mylistId ORDER BY myitem_id DESC")
//  fun getAll(mylistId: Int): Flow<List<Myitem>>

  @Insert
  suspend fun insertAll(vararg myitem: Myitem)

  @Query("SELECT max(myitem_order) FROM myitem where mylist_id = :mylistId")
  fun getMaxOrder(mylistId: Int): Int?

  @Query("update myitem set myitem_order = myitem_order - 1 where mylist_id = :mylistId and myitem_order > :order")
  suspend fun decrementOrder(mylistId: Int, order: Int)

  @Delete
  suspend fun delete(vararg myitem: Myitem)

  @Query("UPDATE myitem set myitem_text = :myitemText WHERE myitem_id = :myitemId")
  suspend fun updateMyitemText(myitemText: String, myitemId: Int)

  @Update
  suspend fun update(vararg myitems: Myitem)
}