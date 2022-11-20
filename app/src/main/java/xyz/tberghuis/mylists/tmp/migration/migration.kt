package xyz.tberghuis.mylists.tmp.migration

import android.database.sqlite.SQLiteDatabase
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.util.logd

class MigrationSingleton {
  val helloMigration = "hello migration"
}

val ms = MigrationSingleton()

@Composable
fun MigrationScreen() {
  val scope = rememberCoroutineScope()

  Column {
    Button(onClick = {
      logd("run sql")
      scope.launch(Dispatchers.IO) {
        openDb()
      }
    }) {
      Text("run sql test")
    }
  }
}

fun openDb() {
  val dbpath = "/data/data/xyz.tberghuis.mylists/databases/mylists-v1.db"
  val db = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE)

//  val result = db.execSQL("ALTER TABLE myitem ADD COLUMN myitem_order integer default 0 not null")
  //  logd("result $result")

  val c1 = db.rawQuery("select distinct mylist_id from myitem", null)
  var stat1 = c1.moveToFirst()
  while (stat1) {
    val mylistId = c1.getInt(c1.getColumnIndexOrThrow("mylist_id"))
    logd("mylistId $mylistId")

    updateMylistOrder(db, mylistId)

    stat1 = c1.moveToNext()
  }
  c1.close()


}

fun updateMylistOrder(db: SQLiteDatabase, mylistId: Int) {
//  val c1 = db.rawQuery("select myitem_id from mylist where mylist_id = ?", arrayOf("1"))
  // dont worry about messing up the order
  val c1 = db.rawQuery("select myitem_id from myitem where mylist_id = $mylistId", null)
  var stat1 = c1.moveToFirst()
  var order = 0
  while (stat1) {
    val myitem_id = c1.getInt(c1.getColumnIndexOrThrow("myitem_id"))
    logd("mylistId $mylistId myitem_id $myitem_id")

    // update order
    db.execSQL("update myitem set myitem_order = $order where myitem_id = $myitem_id and mylist_id = $mylistId")

    order++
    stat1 = c1.moveToNext()
  }
  c1.close()
}
