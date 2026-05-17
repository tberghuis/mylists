package xyz.tberghuis.mylists.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import xyz.tberghuis.mylists.components.BackupButton
import xyz.tberghuis.mylists.components.ImportButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen() {
  Scaffold(topBar = {
    TopAppBar(
      // TODO back/up home arrow
      title = { Text("Backup") }
    )
  }) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .padding(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        BackupButton()
        ImportButton()
      }
    }
  }
  ImportAlertDialog()
}

@Composable
fun ImportAlertDialog(
  vm: BackupViewModel = koinViewModel(),
) {
  val close = { vm.importDialog = false }
  val import = vm.onClickImportHandler()

  if (vm.importDialog) {
    AlertDialog(
      onDismissRequest = close,
      title = {
        Text(text = "Warning")
      },
      text = {
        Text("Importing will delete all your current lists")
      },
      confirmButton = {
        Button(
          onClick = {
            close()
            import()
          }
        ) {
          Text("Confirm")
        }
      },
      dismissButton = {
        Button(
          onClick = close
        ) {
          Text("Cancel")
        }
      }
    )
  }
}