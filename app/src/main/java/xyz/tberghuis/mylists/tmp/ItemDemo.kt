package xyz.tberghuis.mylists.tmp

import androidx.annotation.NonNull
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(
  tableName = "item_demo",
)
data class ItemDemo(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "item_id")
  val itemId: Int = 0,
  @NonNull @ColumnInfo(name = "item_text") val itemText: String,
  @NonNull @ColumnInfo(name = "item_order") val itemOrder: Int
)

@Dao
interface ItemDemoDao {

  @Query("SELECT * FROM item_demo ORDER BY item_order")
  fun getAll(): Flow<List<ItemDemo>>

  @Insert
  suspend fun insertAll(vararg items: ItemDemo)

  @Delete
  suspend fun delete(vararg items: ItemDemo)



  @Query("delete from item_demo")
  suspend fun deleteAll()


//  @Query("UPDATE myitem set myitem_text = :myitemText WHERE myitem_id = :myitemId")
//  suspend fun updateMyitemText(myitemText: String, myitemId: Int)
}