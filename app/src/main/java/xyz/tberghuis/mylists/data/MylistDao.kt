package xyz.tberghuis.mylists.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MylistDao {

  // will probably want to order by modified time???
  @Query("SELECT * FROM mylist ORDER BY mylist_text ASC")
  fun getAll(): Flow<List<Mylist>>

  @Query("SELECT COUNT(myitem_id) FROM myitem WHERE mylist_id = :mylistId")
  fun getMyitemCount(mylistId: Int): LiveData<Int>

  // todo getAllListNameString
  // flow map... somehow
  // nah this will not be needed as I do need ID in composables

  @Query("SELECT mylist_text FROM mylist WHERE mylist_id = :mylistId")
  fun getMylistText(mylistId: Int): LiveData<String>


  @Query("UPDATE mylist set mylist_text = :mylistText WHERE mylist_id = :mylistId")
  suspend fun updateMylistText(mylistText: String, mylistId: Int)

  @Query("UPDATE mylist set myitem_draft_text = :myitemDraftText WHERE mylist_id = :mylistId")
  suspend fun updateMyitemDraftText(myitemDraftText: String, mylistId: Int)

  @Query("SELECT myitem_draft_text FROM mylist WHERE mylist_id = :mylistId")
  fun myitemDraftTextFlow(mylistId: Int): Flow<String>

  @Insert
  suspend fun insertAll(vararg mylist: Mylist)

  // will this blend.... probably not but lets try
  // rewrite so param is entity... mey
  @Query("DELETE FROM mylist WHERE mylist_id = :mylistId")
  suspend fun delete(mylistId: Int)
}

