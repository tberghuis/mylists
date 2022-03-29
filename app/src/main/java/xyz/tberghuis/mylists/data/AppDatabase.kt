package xyz.tberghuis.mylists.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import xyz.tberghuis.mylists.util.logd

@Database(
  entities = [Mylist::class, Myitem::class], version = 2, exportSchema = true,
  autoMigrations = [
//    AutoMigration(from = 1, to = 2)
  ]
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun mylistDao(): MylistDao
  abstract fun myitemDao(): MyitemDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
  override fun migrate(database: SupportSQLiteDatabase) {
    logd("running migration")
//
//    database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, `name` TEXT, " +
//      "PRIMARY KEY(`id`))")
  }
}
