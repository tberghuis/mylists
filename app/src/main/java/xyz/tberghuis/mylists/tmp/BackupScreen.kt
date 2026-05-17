package xyz.tberghuis.mylists.tmp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
  vm: BackupViewModel = koinViewModel(),
) {

  val getImportUri = vm.getImportDbFromFilePicker()

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
        Button(onClick = { vm.deleteImportDb() }) {
          Text("delete import db")
        }
      }
      Button(onClick = getImportUri) {
        Text("get import uri")
      }

      Button(onClick = { vm.copyFromUriToTmpFile() }) {
        Text("copy uri to tmp file")
      }

      Button(onClick = { vm.readImportDbData() }) {
        Text("read import db data")
      }
    }
  }
}

