package xyz.tberghuis.mylists.tmp

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.data.BackupSettingsRepository
import xyz.tberghuis.mylists.service.BackupService
import xyz.tberghuis.mylists.service.ImportBackupService
import kotlinx.coroutines.flow.first

class BackupViewModel(
  private val backupSettingsRepository: BackupSettingsRepository,
  private val importBackupService: ImportBackupService,
  private val backupService: BackupService
) : ViewModel() {

  var uploading by mutableStateOf(false)
  var importing by mutableStateOf(false)

  var backupResultStatus by mutableStateOf("")
  var backupResultMessage by mutableStateOf("")

  val host = mutableStateOf("")
  val user = mutableStateOf("")
  val password = mutableStateOf("")
  val filePath = mutableStateOf("")
  val port = mutableStateOf("")

  var fieldsInitialised by mutableStateOf(false)

  val lastBackupTimeFlow = backupSettingsRepository.lastBackupTimeFlow

  init {
    viewModelScope.launch(Dispatchers.IO) {
      // future, do all this in parallel
      host.value = backupSettingsRepository.hostFlow.first()
      user.value = backupSettingsRepository.userFlow.first()
      password.value = backupSettingsRepository.passwordFlow.first()
      filePath.value = backupSettingsRepository.filePathFlow.first()
      port.value = backupSettingsRepository.portFlow.first().toString()
      fieldsInitialised = true
    }
  }

  // doitwrong
  fun updateHost(s: String) {
    host.value = s
    viewModelScope.launch(Dispatchers.IO) {
      backupSettingsRepository.updateHost(s)
    }
  }

  fun updateUser(s: String) {
    user.value = s
    viewModelScope.launch(Dispatchers.IO) {
      backupSettingsRepository.updateUser(s)
    }
  }

  fun updatePassword(s: String) {
    password.value = s
    viewModelScope.launch(Dispatchers.IO) {
      backupSettingsRepository.updatePassword(s)
    }
  }

  fun updateFilePath(s: String) {
    filePath.value = s
    viewModelScope.launch(Dispatchers.IO) {
      backupSettingsRepository.updateFilePath(s)
    }
  }

  fun updatePort(s: String) {
    s.toIntOrNull()?.also {
      port.value = s
      viewModelScope.launch(Dispatchers.IO) {
        backupSettingsRepository.updatePort(it)
      }
    }
  }

  fun backup() {
    viewModelScope.launch(Dispatchers.Default) {
      uploading = true
      val br = backupService.uploadDb(
        user = user.value,
        host = host.value,
        port = port.value.toIntOrNull() ?: return@launch,
        password = password.value,
        filePath = filePath.value,
      )
      backupResultStatus = br.status
      backupResultMessage = br.message
      if (br.status == "success") {
        backupSettingsRepository.updateLastBackupTime(br.time)
      }
      uploading = false
    }
  }

  fun import(activity: Activity) {
    viewModelScope.launch(Dispatchers.Default) {
      importing = true
      importBackupService.import(
        user = user.value,
        host = host.value,
        port = port.value.toIntOrNull() ?: return@launch,
        password = password.value,
        filePath = filePath.value,
        activity = activity
      )
      // activity should be restarted
    }
  }
}