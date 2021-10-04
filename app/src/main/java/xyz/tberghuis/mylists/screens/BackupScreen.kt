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

  val bs = viewModel.backupSettingsStateFlow.collectAsState().value

  Scaffold(topBar = {
    TopAppBar(
      // TODO back/up home arrow
      title = { Text("Backup") }

    )
  }) {
    Column {
      TextField(
        value = bs.host,
        onValueChange = viewModel::updateHost,
        label = { Text("host") }
      )
      TextField(
        value = bs.user,
        onValueChange = viewModel::updateUser,
        label = { Text("user") }
      )
      // todo eye ****
      TextField(
        value = bs.password,
        onValueChange = viewModel::updatePassword,
        label = { Text("password") }
      )
      // todo number input keyboard
      TextField(
        value = bs.port.toString(),
        onValueChange = viewModel::updatePort,
        label = { Text("port") }
      )

      TextField(
        value = bs.filePath,
        onValueChange = viewModel::updateFilePath,
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


        Button(
          onClick = viewModel::import
        ) {
          Text("Import")
        }

      }

      Row {
        Text("Last backup time: ${bs.lastBackupTime}")
      }
      Text("status: ${viewModel.backupResultStatus}")
      Text(viewModel.backupResultMessage)
    }
  }


}