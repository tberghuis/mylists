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

  val scope = rememberCoroutineScope()
  var host by remember { mutableStateOf("") }
  var user by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var port by remember { mutableStateOf("22") }
  var filePath by remember { mutableStateOf("") }

  var lastBackupTime by remember { mutableStateOf("N/A") }

  LaunchedEffect(Unit) {
    val bs = viewModel.backupSettingsRepository.backupSettingsFlow.first()
    host = bs.host
    user = bs.user
    password = bs.password
    filePath = bs.filePath
    port = bs.port.toString()
  }

  Scaffold(topBar = {
    TopAppBar(
      // TODO back/up home arrow
      title = { Text("Backup") }

    )
  }) {
    Column {

      TextField(
        value = host,
        onValueChange = {
          host = it
        },
        label = { Text("host") }
      )
      TextField(
        value = user,
        onValueChange = {
          user = it
        },
        label = { Text("user") }
      )
      // todo eye ****
      TextField(
        value = password,
        onValueChange = {
          password = it
        },
        label = { Text("password") }
      )
      // todo number input keyboard
      TextField(
        value = port,
        onValueChange = {
          port = it
        },
        label = { Text("port") }
      )

      TextField(
        value = filePath,
        onValueChange = {
          filePath = it
        },
        label = { Text("File path") }
      )


      Row {
        Button(
          onClick = {
            scope.launch(Dispatchers.IO) {
              Log.d("xxx", "user $user")
              val bs = BackupSettings(user, host, port.toInt(), password, filePath)
              viewModel.backupSettingsRepository.save(bs)
            }
          }
        ) {
          Text("save")
        }

        // TODO disable button when uploading
        // todo disable buttons before launchedEffect first() completes
        Button(
          onClick = {
            scope.launch(Dispatchers.Default) {
              BackupService.uploadDb(user, host, port.toInt(), password)
            }
          }
        ) {
          Text("Backup")
        }
      }



      Row {
        Text("Last backup time: $lastBackupTime")
      }
      Text("status of last backup attempt: success || error")
    }
  }


}