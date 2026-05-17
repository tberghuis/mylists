package xyz.tberghuis.mylists.tmp

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.LocalActivity
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
import xyz.tberghuis.mylists.components.BackupButton
import xyz.tberghuis.mylists.components.ImportButton
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
        Button(onClick = {}) {
          Text("delete import db")
        }
      }
    }
  }
}

