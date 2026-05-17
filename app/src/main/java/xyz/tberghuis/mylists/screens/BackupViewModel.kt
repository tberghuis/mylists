package xyz.tberghuis.mylists.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.DB_FILENAME
import xyz.tberghuis.mylists.IMPORT_DB_FILENAME
import xyz.tberghuis.mylists.util.logd
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.room.Room
import java.io.File
import xyz.tberghuis.mylists.data.AppDatabase

class BackupViewModel(
  private val application: Application,
  private val db: AppDatabase,
) : ViewModel() {
  var importDialog by mutableStateOf(false)

  fun backup(backupFileUri: Uri) {
    viewModelScope.launch(IO) {
      // checkpoint
      val query = "pragma wal_checkpoint(full)"
      db.query(query, null).use { cursor ->
        if (cursor.moveToFirst()) {
          val busy = cursor.getInt(0)
          val log = cursor.getInt(1)
          val checkpointed = cursor.getInt(2)
        }
      }

      val dbFile = application.getDatabasePath(DB_FILENAME)
      application.contentResolver.openOutputStream(backupFileUri)?.use { os ->
        dbFile.inputStream().use { fis ->
          fis.copyTo(os)
        }
      }
    }
  }


  @Composable
  fun onClickImportHandler(): () -> Unit {
    val launcher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        logd("rememberLauncherForActivityResult $result")
        logd("launcher result ${result.resultCode}")
        when (result.resultCode) {
          RESULT_OK -> {
            logd("result.data ${result.data}")
            logd("result.data.data ${result.data?.data}")
            result.data?.data?.let {
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

  private fun import(importDbUri: Uri) {
    // delete import db
    application.deleteDatabase(IMPORT_DB_FILENAME)
    // copy uri to temp file
    val importFile = copyFromUriToTempFile(importDbUri) ?: return
    // import data
    readImportDbAndOverwriteDb(importFile)
  }

  private fun copyFromUriToTempFile(importDbUri: Uri): File? {
    val inputStream = application.contentResolver.openInputStream(importDbUri)
    var tempFile: File? = null
    try {
      tempFile = File.createTempFile("temp", "db")
      inputStream?.use { input ->
        tempFile?.outputStream()?.use { output ->
          input.copyTo(output)
        }
      }
    } catch (e: Exception) {
      Log.e("BackupViewModel", "$e")
    }
    return tempFile
  }

  private fun readImportDbAndOverwriteDb(importDb: File) {
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

      // room will not throw errors if import DB is invalid or corrupt
      // room will only log an error
      // only overwrite current DB data if mylists from import isNotEmpty
      if (mylists.isNotEmpty()) {
        // delete all data in db
        db.myitemDao().deleteAll()
        db.mylistDao().deleteAll()

        // insert import db data into mylists.db
        db.mylistDao().insertAll(*mylists.toTypedArray())
        db.myitemDao().insertAll(*myitems.toTypedArray())
      }
      // delete temp file
      importDb.delete()
    }
  }

}