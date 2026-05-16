package xyz.tberghuis.mylists.tmp

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
  viewModel: BackupViewModel = koinViewModel(),
) {

  val context = LocalContext.current
//  var importDialog by remember { mutableStateOf(false) }

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
        Button(
          onClick = viewModel::backup
        ) {
          Text("Backup")
        }

        Button(
          onClick = {
            viewModel.importDialog = true
          }
        ) {
          Text("Import")
        }
      }


    }
  }
  if (viewModel.importDialog) {
    ImportAlertDialog(import = { }, close = { viewModel.importDialog = false })
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

