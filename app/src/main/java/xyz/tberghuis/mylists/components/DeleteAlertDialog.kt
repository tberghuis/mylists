package xyz.tberghuis.mylists.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteAlertDialog(
  onDelete: () -> Unit,
  onCancel: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onCancel,
    title = {
      Text(text = "Confirm Delete")
    },
    text = {
      Text("Are you sure?")
    },
    confirmButton = {
      Button(
        onClick = onDelete
      ) {
        Text("Delete")
      }
    },
    dismissButton = {
      Button(
        onClick = onCancel
      ) {
        Text("Cancel")
      }
    }
  )
}
