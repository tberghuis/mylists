package xyz.tberghuis.mylists.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import xyz.tberghuis.mylists.data.BackupSettingsRepository
import javax.inject.Inject


@HiltViewModel
class BackupViewModel @Inject constructor(
  val backupSettingsRepository: BackupSettingsRepository,
) : ViewModel() {
  val doesitblend = "doesitblend"

  val userFlow = backupSettingsRepository.backupSettingsFlow.map {
    it.user
  }


//  val userLiveData = userFlow.asLiveData()
}