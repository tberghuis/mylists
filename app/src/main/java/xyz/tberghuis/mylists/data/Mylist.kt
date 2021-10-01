package xyz.tberghuis.mylists.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mylist")
data class Mylist(

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "mylist_id")
  val mylistId: Int = 0,

  @NonNull @ColumnInfo(name = "mylist_text") val mylistText: String,

  @NonNull @ColumnInfo(name = "myitem_draft_text") val myitemDraftText: String
)