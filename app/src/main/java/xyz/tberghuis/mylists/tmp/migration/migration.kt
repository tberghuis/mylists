package xyz.tberghuis.mylists.tmp.migration

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

class MigrationSingleton {
  val helloMigration = "hello migration"
}

val ms = MigrationSingleton()

@Composable
fun MigrationScreen() {
  Text("hello migration")
}