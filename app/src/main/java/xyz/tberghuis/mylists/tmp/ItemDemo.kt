package xyz.tberghuis.mylists.tmp

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize


@Entity(
  tableName = "item_demo",
)
@Parcelize
data class ItemDemo(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "item_id")
  val itemId: Int = 0,
  @NonNull @ColumnInfo(name = "item_text") val itemText: String,
  @NonNull @ColumnInfo(name = "item_order") val itemOrder: Int
) : Parcelable

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

  @Update
  suspend fun update(vararg items: ItemDemo)

}