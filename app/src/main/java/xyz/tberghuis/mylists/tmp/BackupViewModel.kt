package xyz.tberghuis.mylists.tmp

import android.app.Application
import androidx.lifecycle.ViewModel
import xyz.tberghuis.mylists.data.AppDatabase
import xyz.tberghuis.mylists.util.logd

class BackupViewModel(
  private val application: Application,
  private val db: AppDatabase,
) : ViewModel() {

  fun deleteImportDb() {
    logd("deleteImportDb")
  }
}