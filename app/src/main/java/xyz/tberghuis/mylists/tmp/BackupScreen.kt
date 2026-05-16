package xyz.tberghuis.mylists.tmp

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import xyz.tberghuis.mylists.util.logd

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
  viewModel: BackupViewModel = koinViewModel(),
) {
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
//  if (viewModel.importDialog) {
//
//  }
  ImportAlertDialog()
}

@Composable
fun ImportAlertDialog(
  vm: BackupViewModel = koinViewModel(),
) {
//  if (!vm.importDialog) {
//    return
//  }
  val close = { vm.importDialog = false }
  val launcher =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      logd("rememberLauncherForActivityResult $result")
      logd("launcher result ${result.resultCode}")

      when (result.resultCode) {
        RESULT_OK -> {
          logd("result.data ${result.data}")
          logd("result.data.data ${result.data?.data}")

          result.data?.data?.let { vm.import(it) }
        }
      }
    }

  fun import() {
    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
      type = "*/*"
      addCategory(Intent.CATEGORY_OPENABLE)
    }
    launcher.launch(intent)
  }

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