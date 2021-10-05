package xyz.tberghuis.mylists.screens

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
//  private val flushService: TmpFlushService
) : ViewModel() {

  // this class is for things like password field eye state


//  var host by mutableStateOf("")
//  var user by mutableStateOf("")
//  var password by mutableStateOf("")
//  var port by mutableStateOf("22")
//  var filePath by mutableStateOf("")

  val backupSettingsStateFlow = MutableStateFlow(BackupSettings())

  var uploading by mutableStateOf(false)

  //  var lastBackupTime by mutableStateOf("")
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
      // try???
      backupSettingsRepository.save(backupSettingsStateFlow.value.copy(port = port.toInt()))
    }
  }


  fun save() {
    viewModelScope.launch(Dispatchers.IO) {
//      Log.d("xxx", "user $user")
//      val bs = BackupSettings(user, host, port.toInt(), password, filePath, lastBackupTime)
      backupSettingsRepository.save(backupSettingsStateFlow.value)
    }
  }

  fun backup() {
    viewModelScope.launch(Dispatchers.Default) {

//      val bs = backupSettingsStateFlow.value
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

  fun import() {
    viewModelScope.launch(Dispatchers.Default) {
//      BackupService.importDb()
      importBackupService.import(backupSettingsStateFlow.value)
    }
  }


  fun flushWal() {
    viewModelScope.launch(Dispatchers.IO) {
//      flushService.flushDbWal()
    }


  }


}