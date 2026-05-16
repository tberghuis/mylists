package xyz.tberghuis.mylists.tmp

import android.app.Activity
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import java.io.File
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.DB_FILENAME
import xyz.tberghuis.mylists.data.AppDatabase
import xyz.tberghuis.mylists.service.triggerRestart
import xyz.tberghuis.mylists.util.logd

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

  fun import(activity: Activity, filePickerUri: Uri) {
    logd("filePickerUri $filePickerUri")
    viewModelScope.launch(IO) {

      val importDbPath = application.getDatabasePath("import-mylists.db").absolutePath
      val importDbFile = File(importDbPath)
      val inputStream = application.contentResolver.openInputStream(filePickerUri)

      try {
        // copy from filePickerUri to tmp db file
        inputStream?.use { input ->
          importDbFile.outputStream().use { output ->
            input.copyTo(output)
          }
        }

        // test if tmp db file is a valid room database
        val roomImport = Room.databaseBuilder(
          application,
          AppDatabase::class.java,
          importDbFile.path
        )
          .build()

//        val roomImport = Room.databaseBuilder(
//          application,
//          AppDatabase::class.java,
//          filePickerUri.ab
//        )
//          .build()
        logd("roomImport $roomImport")
        db.close()
        importDbFile.copyTo(application.getDatabasePath(DB_FILENAME), overwrite = true)
        triggerRestart(activity)
      } catch (e: Exception) {
        Log.e("BackupViewModel", "$e")
      }

    }
  }


}