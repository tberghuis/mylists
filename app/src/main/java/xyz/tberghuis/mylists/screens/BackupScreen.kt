package xyz.tberghuis.mylists.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
//  Text("hello backup")

  // should i collect in launchedEffect
  val userDataStoreState = viewModel.userFlow.collectAsState(initial = "")

//  val userLiveDataState = viewModel.userLiveData.observeAsState(initial = "")


  val scope = rememberCoroutineScope()
  var host by remember { mutableStateOf("") }
  var user by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var port by remember { mutableStateOf("22") }

  var lastBackupTime by remember { mutableStateOf("N/A") }


//  val updateDataStore = {
//    val bs = BackupSettings(user, host, port.toInt(), password)
//    scope.launch(Dispatchers.IO) {
//      viewModel.backupSettingsRepository.testWriteDataStore(bs)
//    }
//  }

  Scaffold(topBar = {
    TopAppBar(
      // TODO back/up home arrow
      title = { Text("Backup") }

    )
  }) {
    Column {

      Text(userDataStoreState.value)
//      Text(userLiveDataState.value)


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
      TextField(
        value = password,
        onValueChange = {
          password = it

        },
        label = { Text("password") }
      )

      TextField(
        value = port,
        onValueChange = {
          port = it

        },
        label = { Text("port") }
      )

      Button(
        onClick = {
          scope.launch(Dispatchers.IO) {

            Log.d("xxx", "user $user")
            val bs = BackupSettings(user, "host", 22, "password")
            viewModel.backupSettingsRepository.testWriteDataStore(bs)
          }
        }
      ) {
        Text("save this is tmp")
      }


      Button(
        onClick = {
          scope.launch(Dispatchers.IO) {


            val prefData = viewModel.backupSettingsRepository.dataStore.data.first()
            Log.d("xxx", prefData.toString())
          }
        }
      ) {
        Text("read datastore")
      }

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
        Text("Last backup time: $lastBackupTime")
      }
      Text("status of last backup attempt: success || error")
    }
  }


}