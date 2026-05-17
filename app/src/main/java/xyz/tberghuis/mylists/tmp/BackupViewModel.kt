package xyz.tberghuis.mylists.tmp

import android.app.Application
import androidx.lifecycle.ViewModel
import xyz.tberghuis.mylists.IMPORT_DB_FILENAME
import xyz.tberghuis.mylists.data.AppDatabase
import xyz.tberghuis.mylists.util.logd

class BackupViewModel(
  private val application: Application,
  private val db: AppDatabase,
) : ViewModel() {

  fun deleteImportDb() {
    logd("deleteImportDb")
//    application.deleteDatabase("import-mylists.db")
//    application.deleteDatabase("tmp.db")
    application.deleteDatabase(IMPORT_DB_FILENAME)
  }
  
  
}