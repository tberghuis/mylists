package xyz.tberghuis.mylists.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.service.BackupService


@Composable
fun BackupScreen() {
//  Text("hello backup")

  val scope = rememberCoroutineScope()
  var host by remember { mutableStateOf("") }
  var user by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var port by remember { mutableStateOf("22") }

  var lastBackupTime by remember { mutableStateOf("N/A") }


  Scaffold(topBar = {
    TopAppBar(
      // TODO back/up home arrow
      title = { Text("Backup") }

    )
  }) {
    Column {
      TextField(
        value = host,
        onValueChange = { host = it },
        label = { Text("host") }
      )
      TextField(
        value = user,
        onValueChange = { user = it },
        label = { Text("user") }
      )
      TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("password") }
      )

      TextField(
        value = port,
        onValueChange = { port = it },
        label = { Text("port") }
      )

      // TODO disable button when uploading
      Button(
        onClick = {
          scope.launch(Dispatchers.Default) {

            BackupService.uploadDb(user, host, port.toInt(), password)
          }
        }
      ) {
        Text("Backup")
      }

      Row {
        Text("Last backup: $lastBackupTime")
      }
    }
  }


}