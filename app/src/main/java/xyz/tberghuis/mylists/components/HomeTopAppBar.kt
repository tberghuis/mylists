package xyz.tberghuis.mylists.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.navigation.NavHostController

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
      DropdownMenuItem(onClick = {
        menuExpanded = false
        // navigate to backup
        navHostController.navigate("backup")

      }) {
        Text("Backup")
      }
    }
  })
}