package xyz.tberghuis.mylists.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
  entities = [Mylist::class, Myitem::class], version = 1, exportSchema = true,
  autoMigrations = [
//    AutoMigration(from = 1, to = 2)
  ]
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun mylistDao(): MylistDao
  abstract fun myitemDao(): MyitemDao
}