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
    database.execSQL("ALTER TABLE myitem ADD COLUMN myitem_order integer default 0 not null")
    val c1 = database.query("select distinct mylist_id from myitem", null)
    var stat1 = c1.moveToFirst()
    while (stat1) {
      val mylistId = c1.getInt(c1.getColumnIndexOrThrow("mylist_id"))
      logd("mylistId $mylistId")
      updateMylistOrder(database, mylistId)
      stat1 = c1.moveToNext()
    }
    c1.close()
  }

  fun updateMylistOrder(db: SupportSQLiteDatabase, mylistId: Int) {
//  val c1 = db.rawQuery("select myitem_id from mylist where mylist_id = ?", arrayOf("1"))
    // dont worry about messing up the order

    val c1 = db.query("select myitem_id from myitem where mylist_id = $mylistId", null)
    var stat1 = c1.moveToFirst()
    var order = 0
    while (stat1) {
      val myitemId = c1.getInt(c1.getColumnIndexOrThrow("myitem_id"))
      logd("mylistId $mylistId myitem_id $myitemId")
      // update order
      db.execSQL("update myitem set myitem_order = $order where myitem_id = $myitemId and mylist_id = $mylistId")
      order++
      stat1 = c1.moveToNext()
    }
    c1.close()
  }
}