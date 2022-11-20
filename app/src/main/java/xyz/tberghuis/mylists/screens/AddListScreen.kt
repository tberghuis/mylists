package xyz.tberghuis.mylists.screens

import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun AddListScreen(
  viewModel: AddListViewModel = hiltViewModel(),
  navController: NavHostController
) {

  val focusRequester = remember { FocusRequester() }

  val addList: () -> Unit = {
    viewModel.addListName()
    // should i be doing navigateUp???
    navController.popBackStack()
  }

  Scaffold(topBar = {
    TopAppBar(
      title = { Text("Add List") },
    )
  }) { paddingValues ->


    Column(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      Row {
        TextField(
          value = viewModel.mylistTextField.value,
          singleLine = true,
          keyboardActions = KeyboardActions(
            onDone = { addList() }
          ),
          onValueChange = {
            viewModel.mylistTextField.value = it
          },
          label = { Text("List Name") },
          modifier = Modifier
            .focusRequester(focusRequester)
            .onKeyEvent {
              if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                addList()
                // doesn't prevent \n being added to mylistTextField ???
                return@onKeyEvent true
              }
              false
            }
            .fillMaxWidth()

        )
      }


      Row(
        modifier = Modifier.padding(top = 16.dp)
      ) {
        Button(
          enabled = viewModel.mylistTextField.value.isNotBlank(),
          onClick = addList,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Add List")
        }
      }


    }
  }

//  https://stackoverflow.com/questions/64181930/request-focus-on-textfield-in-jetpack-compose
// doesnt work????
//  DisposableEffect(Unit) {
//    focusRequester.requestFocus()
//    onDispose { }
//  }

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

}
