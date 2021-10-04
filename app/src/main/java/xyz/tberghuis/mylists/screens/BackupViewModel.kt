package xyz.tberghuis.mylists.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.data.BackupSettings
import xyz.tberghuis.mylists.data.BackupSettingsRepository
import xyz.tberghuis.mylists.service.BackupService
import xyz.tberghuis.mylists.service.ImportBackupService
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
  private val backupSettingsRepository: BackupSettingsRepository,
  private val importBackupService: ImportBackupService
) : ViewModel() {

  // this class is for things like password field eye state


  var host by mutableStateOf("")
  var user by mutableStateOf("")
  var password by mutableStateOf("")
  var port by mutableStateOf("22")
  var filePath by mutableStateOf("")

  var lastBackupTime by mutableStateOf("")
  var backupResultStatus by mutableStateOf("")
  var backupResultMessage by mutableStateOf("")

  init {
    viewModelScope.launch(Dispatchers.IO) {
      // this should be state flow.... or not???
      // remove save button
      // one way data flow
      val bs = backupSettingsRepository.backupSettingsFlow.first()
      host = bs.host
      user = bs.user
      password = bs.password
      filePath = bs.filePath
      port = bs.port.toString()
      lastBackupTime = bs.lastBackupTime
    }
  }

  fun save() {
    viewModelScope.launch(Dispatchers.IO) {
      Log.d("xxx", "user $user")
      val bs = BackupSettings(user, host, port.toInt(), password, filePath, lastBackupTime)
      backupSettingsRepository.save(bs)
    }
  }

  fun backup() {
    viewModelScope.launch(Dispatchers.Default) {
      val br = BackupService.uploadDb(user, host, port.toInt(), password, filePath)
      backupResultStatus = br.status
      backupResultMessage = br.message

      if (br.status == "success") {
        lastBackupTime = br.time
        backupSettingsRepository.saveBackupTime(br.time)
      }

    }
  }

  fun import() {
    viewModelScope.launch(Dispatchers.Default) {
//      BackupService.importDb()
      importBackupService.import(user, host, port.toInt(), password, filePath)
    }
  }


}