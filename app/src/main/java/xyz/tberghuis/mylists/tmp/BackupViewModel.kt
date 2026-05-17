package xyz.tberghuis.mylists.tmp

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
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


//  var importDbUri: Uri? = null
//  var importDbFile: File? = null

//  fun deleteImportDb() {
//    logd("deleteImportDb")
//    application.deleteDatabase(IMPORT_DB_FILENAME)
//  }

  @Composable
  fun launchImport(): () -> Unit {
    val launcher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        logd("rememberLauncherForActivityResult $result")
        logd("launcher result ${result.resultCode}")
        when (result.resultCode) {
          RESULT_OK -> {
            logd("result.data ${result.data}")
            logd("result.data.data ${result.data?.data}")
            result.data?.data?.let {
//              importDbUri = it
              import(it)
            }
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

  private fun copyFromUriToTmpFile(importDbUri: Uri): File? {
    val inputStream = application.contentResolver.openInputStream(importDbUri)
    var tmpFile: File? = null
    try {
      tmpFile = File.createTempFile("tmp", "db")
      inputStream?.use { input ->
        tmpFile?.outputStream()?.use { output ->
          input.copyTo(output)
        }
      }
    } catch (e: Exception) {
      Log.e("BackupViewModel", "$e")
    }
    return tmpFile
  }


  private fun readImportDbDataAndOverwriteDb(importDb: File) {
    viewModelScope.launch(IO) {
      val roomImport = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        IMPORT_DB_FILENAME
      )
        .createFromFile(importDb)
        .build()
      val mylists = roomImport.mylistDao().getAll().first()
      logd("mylists $mylists")
      val myitems = roomImport.myitemDao().getAll().first()
      logd("myitems $myitems")

      if (mylists.isNotEmpty()) {
        // delete all data in db
        db.myitemDao().deleteAll()
        db.mylistDao().deleteAll()

        // insert import db data into mylists.db
        db.mylistDao().insertAll(*mylists.toTypedArray())
        db.myitemDao().insertAll(*myitems.toTypedArray())
      }
    }
  }

  private fun import(importDbUri: Uri) {
    // delete import db
    application.deleteDatabase(IMPORT_DB_FILENAME)
    // copy uri to tmp file
    val importFile = copyFromUriToTmpFile(importDbUri) ?: return
    // read import data
    readImportDbDataAndOverwriteDb(importFile)
  }
}
