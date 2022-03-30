package xyz.tberghuis.mylists.tmp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
  entities = [ItemDemo::class], version = 1,

)
abstract class DemoDatabase : RoomDatabase() {
  abstract fun itemDemoDao(): ItemDemoDao

}