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
    logd("running migration willitblend")

    // todo alter table add myitem_order column
    // can i run sql update statements here???
    // test

    database.execSQL("ALTER TABLE myitem ADD COLUMN myitem_order integer not null")
//    database.execSQL("INSERT INTO myitem VALUES(1,1,'li',0)")
  }
}

// reset DB only used in dev
val MIGRATION_2_1 = object : Migration(2, 1) {
  override fun migrate(database: SupportSQLiteDatabase) {
    logd("reset migration")
//    database.beginTransaction()
    database.execSQL("drop table myitem")
    database.execSQL("CREATE TABLE `myitem` (`myitem_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mylist_id` INTEGER NOT NULL, `myitem_text` TEXT NOT NULL, FOREIGN KEY(`mylist_id`) REFERENCES `mylist`(`mylist_id`) ON UPDATE CASCADE ON DELETE CASCADE )")
    database.execSQL("CREATE INDEX `index_myitem_mylist_id` ON `myitem` (`mylist_id`)")
  }
}