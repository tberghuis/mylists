package xyz.tberghuis.mylists.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.data.BackupSettings
import xyz.tberghuis.mylists.service.BackupService


@Composable
fun BackupScreen(
  viewModel: BackupViewModel = hiltViewModel(),
) {
//  var lastBackupTime by remember { mutableStateOf("N/A") }

  Scaffold(topBar = {
    TopAppBar(
      // TODO back/up home arrow
      title = { Text("Backup") }

    )
  }) {
    Column {

      TextField(
        value = viewModel.host,
        onValueChange = {
          viewModel.host = it
        },
        label = { Text("host") }
      )
      TextField(
        value = viewModel.user,
        onValueChange = {
          viewModel.user = it
        },
        label = { Text("user") }
      )
      // todo eye ****
      TextField(
        value = viewModel.password,
        onValueChange = {
          viewModel.password = it
        },
        label = { Text("password") }
      )
      // todo number input keyboard
      TextField(
        value = viewModel.port,
        onValueChange = {
          viewModel.port = it
        },
        label = { Text("port") }
      )

      TextField(
        value = viewModel.filePath,
        onValueChange = {
          viewModel.filePath = it
        },
        label = { Text("File path") }
      )

      Row {
        Button(
          onClick = viewModel::save
        ) {
          Text("save")
        }

        // TODO disable button when uploading
        // todo disable buttons before launchedEffect first() completes
        Button(
          onClick = viewModel::backup
        ) {
          Text("Backup")
        }
      }

      Row {
        Text("Last backup time: ${viewModel.lastBackupTime}")
      }
      Text("status: ${viewModel.backupResultStatus}")
      Text(viewModel.backupResultMessage)
    }
  }


}