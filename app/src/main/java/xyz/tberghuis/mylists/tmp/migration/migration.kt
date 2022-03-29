package xyz.tberghuis.mylists.tmp.migration

import android.database.sqlite.SQLiteDatabase
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
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
  val dbpath = "/data/data/xyz.tberghuis.mylists/databases/bak.mylists.db"
  val db = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE)
  val result = db.rawQuery("select * from mylist", null)
  logd("result $result")
}