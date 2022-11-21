package xyz.tberghuis.mylists.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
