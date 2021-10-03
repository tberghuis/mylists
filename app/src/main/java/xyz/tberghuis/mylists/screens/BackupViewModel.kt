package xyz.tberghuis.mylists.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.tberghuis.mylists.data.BackupSettingsRepository
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
  val backupSettingsRepository: BackupSettingsRepository,
) : ViewModel() {

  // this class is for things like password field eye state
}