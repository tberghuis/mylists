package xyz.tberghuis.mylists.tmp

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BackupViewModel(

) : ViewModel() {
  var importDialog by mutableStateOf(false)

  fun backup(backupFileUri: Uri) {

  }

}