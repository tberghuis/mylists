package xyz.tberghuis.mylists.screens

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.data.BackupSettings
import xyz.tberghuis.mylists.data.BackupSettingsRepository
import xyz.tberghuis.mylists.service.BackupService
import xyz.tberghuis.mylists.service.ImportBackupService
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import xyz.tberghuis.mylists.service.TmpFlushService

@HiltViewModel
class BackupViewModel @Inject constructor(
  private val backupSettingsRepository: BackupSettingsRepository,
  private val importBackupService: ImportBackupService,
  private val backupService: BackupService
) : ViewModel() {

  // this class is for things like password field eye state

  val backupSettingsStateFlow = MutableStateFlow(BackupSettings())

  var uploading by mutableStateOf(false)
  var importing by mutableStateOf(false)

  var backupResultStatus by mutableStateOf("")
  var backupResultMessage by mutableStateOf("")

  init {
    viewModelScope.launch(Dispatchers.IO) {
      // this should be state flow.... or not???
      // remove save button
      // one way data flow
      backupSettingsRepository.backupSettingsFlow.collect { bs ->
        backupSettingsStateFlow.value = bs
      }
//      host = bs.host
//      user = bs.user
//      password = bs.password
//      filePath = bs.filePath
//      port = bs.port.toString()
//      lastBackupTime = bs.lastBackupTime
    }
  }

  // do this inefficient as i don't know any better yet
  // read source of data class copy
  // i probably want custom validation so this way probably ain't so bad

  fun updateHost(host: String) {
    viewModelScope.launch(Dispatchers.IO) {
      backupSettingsRepository.save(backupSettingsStateFlow.value.copy(host = host))
    }
  }

  fun updateUser(user: String) {
    viewModelScope.launch(Dispatchers.IO) {
      backupSettingsRepository.save(backupSettingsStateFlow.value.copy(user = user))
    }
  }

  fun updatePassword(password: String) {
    viewModelScope.launch(Dispatchers.IO) {
      backupSettingsRepository.save(backupSettingsStateFlow.value.copy(password = password))
    }
  }

  fun updateFilePath(filePath: String) {
    viewModelScope.launch(Dispatchers.IO) {
      backupSettingsRepository.save(backupSettingsStateFlow.value.copy(filePath = filePath))
    }
  }

  fun updatePort(port: String) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        backupSettingsRepository.save(backupSettingsStateFlow.value.copy(port = port.toInt()))
      } catch (e: NumberFormatException) {
//        println(e)
      }
    }
  }

  fun backup() {
    viewModelScope.launch(Dispatchers.Default) {
      uploading = true
      val br = backupService.uploadDb(backupSettingsStateFlow.value)
      backupResultStatus = br.status
      backupResultMessage = br.message
      if (br.status == "success") {
        val bs = backupSettingsStateFlow.value.copy(lastBackupTime = br.time)
        backupSettingsStateFlow.value = bs
        backupSettingsRepository.saveBackupTime(br.time)
      }
      uploading = false
    }
  }

  fun import(activity: Activity) {
    viewModelScope.launch(Dispatchers.Default) {
      importing = true
      importBackupService.import(backupSettingsStateFlow.value, activity)
      // activity should be restarted
    }
  }
}