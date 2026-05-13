package xyz.tberghuis.mylists.tmp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel


@Composable
fun Screen(viewModel: HelloViewModel = koinViewModel()) {
  Column {
    Text(
      text = "Hello ${viewModel.willitblend}!",
    )
    Button(onClick = { viewModel.logDb() }) {
      Text("log db")
    }
  }

}
