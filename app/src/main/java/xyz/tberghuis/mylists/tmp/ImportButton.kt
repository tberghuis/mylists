package xyz.tberghuis.mylists.tmp

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import xyz.tberghuis.mylists.screens.BackupViewModel

@Composable
fun ImportButton(
  vm: BackupViewModel = koinViewModel(),
) {
  Button(
    onClick = {
      vm.importDialog = true
    }
  ) {
    Text("Import")
  }
}