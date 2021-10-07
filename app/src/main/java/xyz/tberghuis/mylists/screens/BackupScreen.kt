package xyz.tberghuis.mylists.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun BackupScreen(
  viewModel: BackupViewModel = hiltViewModel(),
) {
  val bs = viewModel.backupSettingsStateFlow.collectAsState().value
  val actionsEnabled = !(viewModel.uploading || viewModel.importing)
  var passwordVisibility by remember { mutableStateOf(false) }
  val context = LocalContext.current
  var importDialog by remember { mutableStateOf(false) }

  Scaffold(topBar = {
    TopAppBar(
      // TODO back/up home arrow
      title = { Text("Backup") }
    )
  }) {
    Column(
      modifier = Modifier.padding(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
      // https://stackoverflow.com/questions/65304229/toggle-password-field-jetpack-compose
      TextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        value = bs.password,
        onValueChange = viewModel::updatePassword,
        label = { Text("password") },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
          val image = if (passwordVisibility)
            Icons.Filled.Visibility
          else Icons.Filled.VisibilityOff
          IconButton(onClick = {
            passwordVisibility = !passwordVisibility
          }) {
            Icon(imageVector = image, "")
          }
        }
      )
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

      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
//            viewModel.import(context as Activity)
            importDialog = true
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

  if (importDialog) {
    ImportAlertDialog({ viewModel.import(context as Activity) }, { importDialog = false })
  }

}


@Composable
fun ImportAlertDialog(
  import: () -> Unit,
  close: () -> Unit
) {
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

