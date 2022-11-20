package xyz.tberghuis.mylists.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
  navHostController: NavHostController,
) {
  var menuExpanded by remember { mutableStateOf(false) }

  TopAppBar(title = { Text("My Lists") }, actions = {
    IconButton(onClick = {
      menuExpanded = true
    }) {
      Icon(
        Icons.Filled.MoreVert, contentDescription = "More Menu"
      )
    }

    DropdownMenu(
      expanded = menuExpanded,
      onDismissRequest = { menuExpanded = false },
    ) {
      DropdownMenuItem(
        text = { Text("Backup") },
        onClick = {
          menuExpanded = false
          // navigate to backup
          navHostController.navigate("backup")
        })
    }
  })
}