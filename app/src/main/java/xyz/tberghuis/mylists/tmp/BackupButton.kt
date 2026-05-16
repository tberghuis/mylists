package xyz.tberghuis.mylists.tmp

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun BackupButton(
  viewModel: BackupViewModel = koinViewModel(),
) {
  Button(
    onClick = viewModel::backup
  ) {
    Text("Backup")
  }
}