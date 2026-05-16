package xyz.tberghuis.mylists.screens

import android.app.Activity
import android.app.Application
import android.database.sqlite.SQLiteDatabase
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.DB_FILENAME
import xyz.tberghuis.mylists.IMPORT_DB_FILENAME
import xyz.tberghuis.mylists.data.AppDatabase
import xyz.tberghuis.mylists.data.Mylist
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

      val importDbPath = application.getDatabasePath(IMPORT_DB_FILENAME).absolutePath
      val importDbFile = File(importDbPath)
      val inputStream = application.contentResolver.openInputStream(filePickerUri)

      try {
        // copy from filePickerUri to IMPORT_DB_FILENAME
        inputStream?.use { input ->
          importDbFile.outputStream().use { output ->
            input.copyTo(output)
          }
        }


//        SQLiteDatabase.openDatabase(
//          importDbFile.path,
//          null,
//          SQLiteDatabase.OPEN_READONLY
//        ).use { db ->
//
//          logd("db.version ${db.version}")
//
//
//          val cursor = db.rawQuery("PRAGMA integrity_check", null);
//          if (cursor.moveToFirst()) {
//            val result = cursor.getString(0);
//            if (result != "ok") {
//                logd("import ok")
//            } else {
//                logd("import ok")
//            }
//          }
//          cursor.close();
//
//        }


        // test if IMPORT_DB_FILENAME is a valid room database
        val roomImport = Room.databaseBuilder(
          application,
          AppDatabase::class.java,
//          importDbFile.path
          "tmp.db"
        )
          .createFromFile(importDbFile)

          .build()
        logd("before import")
        val list = roomImport.mylistDao().getAll().first()
        logd("after import")

//        logd("before PRAGMA integrity_check")
//        val cursor = roomImport.openHelper.writableDatabase.query("PRAGMA integrity_check")
//        if (cursor.moveToFirst()) {
//          val result = cursor.getString(0)
//          logd("result $result")
//          if (result != "ok") {
//            // Handle corruption (e.g., delete and recreate)
//            logd("import not ok")
//          } else {
//            logd("import ok")
//          }
//        }
//        cursor.close()
//
//        logd("after PRAGMA integrity_check")

//        logd("roomImport $roomImport")
        db.close()
        importDbFile.copyTo(application.getDatabasePath(DB_FILENAME), overwrite = true)
        triggerRestart(activity)
      } catch (e: Exception) {
        Log.e("BackupViewModel", "$e")
      }
    }
  }
}