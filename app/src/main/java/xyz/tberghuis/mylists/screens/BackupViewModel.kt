package xyz.tberghuis.mylists.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
  private val backupSettingsRepository: BackupSettingsRepository,
) : ViewModel() {

  // this class is for things like password field eye state


  var host by mutableStateOf("")
  var user by mutableStateOf("")
  var password by mutableStateOf("")
  var port by mutableStateOf("22")
  var filePath by mutableStateOf("")

  init {
    viewModelScope.launch(Dispatchers.IO) {
      val bs = backupSettingsRepository.backupSettingsFlow.first()
      host = bs.host
      user = bs.user
      password = bs.password
      filePath = bs.filePath
      port = bs.port.toString()
    }
  }

  fun save() {
    viewModelScope.launch(Dispatchers.IO) {
      Log.d("xxx", "user $user")
      val bs = BackupSettings(user, host, port.toInt(), password, filePath)
      backupSettingsRepository.save(bs)
    }
  }

  fun backup() {
    viewModelScope.launch(Dispatchers.Default) {
      BackupService.uploadDb(user, host, port.toInt(), password)
    }
  }

}