package xyz.tberghuis.mylists.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.tberghuis.mylists.components.HomeTopAppBar
import xyz.tberghuis.mylists.data.Mylist

@Composable
fun HomeScreen(
  navHostController: NavHostController,
  viewModel: HomeViewModel = hiltViewModel()
) {
  // delegate by
  val listNames: List<Mylist> by viewModel.getAllListNames()
    .collectAsState(initial = listOf())

  Scaffold(
    topBar = {
      HomeTopAppBar(navHostController)
    },
    floatingActionButton = {
      FloatingActionButton(onClick = {
        navHostController.navigate("add-list")
      }) {
        Icon(Icons.Filled.Add, "add list")
      }
    }) {

    LazyColumn(
      contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 10.dp)
    ) {
      items(items = listNames) { listName ->
        val count: Int? by viewModel.getCount(listName.mylistId).observeAsState()
        val countString = count?.toString() ?: ""
        Card(
          elevation = 2.dp,
          modifier = Modifier
            .padding(top = 10.dp)
            .clickable {
              navHostController.navigate("list/${listName.mylistId}")
            }
        ) {
          Row(
            modifier = Modifier
              .padding(8.dp)
              .fillMaxWidth()
          ) {
            Text(listName.mylistText, modifier = Modifier.weight(1f))
            // fix design later
            Text(countString, color = Color.Red)
          }
        }
      }
    }
  }
}