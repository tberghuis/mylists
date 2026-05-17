package xyz.tberghuis.mylists.tmp

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import java.io.File
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.IMPORT_DB_FILENAME
import xyz.tberghuis.mylists.data.AppDatabase
import xyz.tberghuis.mylists.util.logd

class BackupViewModel(
  private val application: Application,
  private val db: AppDatabase,
) : ViewModel() {


  var importDbUri: Uri? = null
  var importDbFile: File? = null

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

  fun copyFromUriToTmpFile() {
    val inputStream = application.contentResolver.openInputStream(importDbUri!!)
    importDbFile = File.createTempFile("tmp", "db")
    inputStream?.use { input ->
      importDbFile?.outputStream()?.use { output ->
        input.copyTo(output)
      }
    }
  }


  fun readImportDbData() {

    viewModelScope.launch(IO) {
      val roomImport = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        IMPORT_DB_FILENAME
      )
        .createFromFile(importDbFile!!)
        .build()


      val mylists = roomImport.mylistDao().getAll().first()

      logd("mylists $mylists")

      val myitems = roomImport.myitemDao().getAll().first()

      logd("myitems $myitems")
      
      if(mylists.isNotEmpty()){
        // delete all data in db
//        db.myitemDao().deleteAll
        
        // insert import db data into mylists.db
      }
      

    }
  }
}