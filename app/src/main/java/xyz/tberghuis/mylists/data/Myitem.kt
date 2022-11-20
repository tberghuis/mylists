package xyz.tberghuis.mylists.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// refactor mylist myitem

@Entity(
  tableName = "myitem",
  foreignKeys = [ForeignKey(
    entity = Mylist::class,
    parentColumns = arrayOf("mylist_id"),
    childColumns = arrayOf("mylist_id"),
    onUpdate = ForeignKey.CASCADE,
    onDelete = ForeignKey.CASCADE
  )]
)
@Parcelize
data class Myitem(
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "myitem_id")
  val myitemId: Int = 0,
  @ColumnInfo(name = "mylist_id", index = true) val mylistId: Int,
  @ColumnInfo(name = "myitem_text") val myitemText: String,
  @ColumnInfo(name = "myitem_order") val myitemOrder: Int,
) : Parcelable