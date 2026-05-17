package xyz.tberghuis.mylists.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import xyz.tberghuis.mylists.screens.XxxBackupViewModel

@Composable
fun ImportButton(
  vm: XxxBackupViewModel = koinViewModel(),
) {
  Button(
    onClick = {
      vm.importDialog = true
    }
  ) {
    Text("Import")
  }
}