package xyz.tberghuis.mylists.tmp

import androidx.lifecycle.ViewModel
import xyz.tberghuis.mylists.data.AppDatabase

class HelloViewModel(val db: AppDatabase) : ViewModel() {
  val willitblend = "will it blend"

  fun logDb() {
    println("logDb $db")
  }

}