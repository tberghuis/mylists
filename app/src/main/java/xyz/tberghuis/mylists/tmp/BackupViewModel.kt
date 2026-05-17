package xyz.tberghuis.mylists.tmp

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import xyz.tberghuis.mylists.IMPORT_DB_FILENAME
import xyz.tberghuis.mylists.data.AppDatabase
import xyz.tberghuis.mylists.util.logd

class BackupViewModel(
  private val application: Application,
  private val db: AppDatabase,
) : ViewModel() {


  var importDbUri: Uri? = null

  fun deleteImportDb() {
    logd("deleteImportDb")
//    application.deleteDatabase("import-mylists.db")
//    application.deleteDatabase("tmp.db")
    application.deleteDatabase(IMPORT_DB_FILENAME)
  }

  @Composable
  fun getImportDbFromFilePicker(): () -> Unit {
    val launcher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        logd("rememberLauncherForActivityResult $result")
        logd("launcher result ${result.resultCode}")
        when (result.resultCode) {
          RESULT_OK -> {
            logd("result.data ${result.data}")
            logd("result.data.data ${result.data?.data}")
            result.data?.data?.let { importDbUri = it }
          }
        }
      }
    return {
      val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = "*/*"
        addCategory(Intent.CATEGORY_OPENABLE)
      }
      launcher.launch(intent)
    }
  }


}