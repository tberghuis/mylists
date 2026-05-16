package xyz.tberghuis.mylists.tmp

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import xyz.tberghuis.mylists.DEFAULT_BACKUP_DB_FILENAME
import xyz.tberghuis.mylists.screens.BackupViewModel

@Composable
fun BackupButton(
  vm: BackupViewModel = koinViewModel(),
) {
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/vnd.sqlite3")) { uri ->
      uri?.let {
        vm.backup(uri)
      }
    }

  Button(
    onClick = { launcher.launch(DEFAULT_BACKUP_DB_FILENAME) }
  ) {
    Text("Backup")
  }
}