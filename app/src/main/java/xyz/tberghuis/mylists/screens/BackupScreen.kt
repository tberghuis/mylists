package xyz.tberghuis.mylists.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import xyz.tberghuis.mylists.data.BackupSettings
import xyz.tberghuis.mylists.service.BackupService
import android.app.AlarmManager

import android.app.PendingIntent
import android.content.Context

import android.content.Intent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import xyz.tberghuis.mylists.MainActivity
import kotlin.system.exitProcess


@Composable
fun BackupScreen(
  viewModel: BackupViewModel = hiltViewModel(),
) {
  val bs = viewModel.backupSettingsStateFlow.collectAsState().value
//  val actionEnabled = {
//    !(viewModel.uploading || viewModel.importing)
//  }
  val actionsEnabled = !(viewModel.uploading || viewModel.importing)


  val context = LocalContext.current

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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
        // todo disable buttons before backupsettings flow first collects
        // meh
        Button(
          enabled = actionsEnabled,
          onClick = viewModel::backup
        ) {
          Text("Backup")
        }

        Button(
          enabled = actionsEnabled,
          onClick = {
            viewModel.import(context as Activity)
          }


        ) {
          Text("Import")
        }
      }

      Row {
        Text("Last backup time: ${bs.lastBackupTime}")
      }

      if (!actionsEnabled) {
        Text("processing...")
      } else {
        Text("status: ${viewModel.backupResultStatus}")
        Text(viewModel.backupResultMessage)
      }


    }
  }


}

