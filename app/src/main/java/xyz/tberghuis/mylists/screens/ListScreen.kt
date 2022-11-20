package xyz.tberghuis.mylists.screens

import android.os.Parcelable
import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import xyz.tberghuis.mylists.components.DeleteAlertDialog
import xyz.tberghuis.mylists.data.Myitem
import org.burnoutcrew.reorderable.*
import xyz.tberghuis.mylists.util.logd
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
  viewModel: ListViewModel = hiltViewModel(), navController: NavHostController,
) {
  Scaffold(topBar = { ListScreenTopAppBar(viewModel) }) { paddingValues ->
    Column(Modifier.padding(paddingValues)) {
      DraftTextEntry()
      RenderMyitemList(viewModel)
    }
  }
  ShowDialog(viewModel, navController)
}

@Composable
fun <T : Parcelable> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
  return rememberSaveable(
    saver = listSaver(save = { it.toList() },
      restore = { it.toMutableStateList() })
  ) {
    elements.toList().toMutableStateList()
  }
}


fun onDragEnd(
  myitemList: List<Myitem>, startIndex: Int, endIndex: Int, listViewModel: ListViewModel
) {
  logd("onDragEnd startIndex $startIndex endIndex $endIndex")
  if (startIndex == endIndex) {
    return
  }
  // is there a more elegant way???
  val ml = mutableListOf<Myitem>()
  for (i in min(startIndex, endIndex)..max(startIndex, endIndex)) {
    ml.add(myitemList[i].copy(myitemOrder = i))
  }
  listViewModel.update(*ml.toTypedArray())
}

// bad function name... meh
@Composable
fun RenderMyitemList(viewModel: ListViewModel = hiltViewModel()) {
  val state = rememberReorderState()
  val myitemList = rememberMutableStateListOf<Myitem>()
  LaunchedEffect(Unit) {
    viewModel.getAllMyitems().collect {
      myitemList.clear()
      myitemList.addAll(it)
    }
  }


  LazyColumn(
    contentPadding = PaddingValues(10.dp, 0.dp, 10.dp, 10.dp),
    state = state.listState,
    modifier = Modifier.reorderable(
      state,
      { from, to ->
        myitemList.move(from.index, to.index)
      },
      onDragEnd = { startIndex: Int, endIndex: Int ->
        onDragEnd(myitemList, startIndex, endIndex, viewModel)
      },
    )
  ) {
    items(items = myitemList, { it.myitemId }) { myitem ->
      Card(
        modifier = Modifier
          .padding(top = 10.dp)
          .draggedItem(state.offsetByKey(myitem.myitemId))
          .detectReorderAfterLongPress(state),
//        elevation = 2.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = myitem.myitemText, modifier = Modifier.weight(1f)
          )
          IconButton(onClick = { viewModel.editMyitemDialog = myitem }) {
            Icon(Icons.Filled.Edit, contentDescription = "edit")
          }
          IconButton(onClick = { viewModel.confirmDeleteMyitemDialog = myitem }) {
            Icon(Icons.Filled.Delete, contentDescription = "delete")
          }
        }
      }
    }
  }
}


// this has a race condition, user could theoretically update
// draft text before first draft text flow is collected
// but this would be very unlikely
// need to investigate how to DI mylistId into VM
// i tried a state class but leaving that for an exercise for a fresh project
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftTextEntry(
  viewModel: ListViewModel = hiltViewModel(),
) {
  val draftTextField by viewModel.draftTextFieldStateFlow.collectAsState()
  val addListItem: () -> Unit = {
    draftTextField?.let {
      viewModel.addListItem(it)
    }
  }

  // flow returns a null value when mylist deleted
  // is this a bug with Flow or room?
  if (draftTextField != null) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
    ) {
      TextField(value = draftTextField!!,
        onValueChange = {
          viewModel.updateMylistDraftText(draftText = it)
        },
        label = { Text("List Item") },
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = {
          // Log.d("xxx", "on done")
          addListItem()
        }),
        modifier = Modifier
          .weight(1f)
          // make this reusable somehow, extension function
          .onKeyEvent {
            if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
              addListItem()
              // doesn't prevent \n being added to mylistTextField ???
              return@onKeyEvent true
            }
            false
          })
      Button(
        enabled = draftTextField!!.isNotBlank(),
        onClick = addListItem,
        modifier = Modifier.padding(start = 8.dp)
      ) {
        Icon(Icons.Filled.Add, "add list item")
      }
    }
  }
}

@Composable
fun ShowDialog(
  viewModel: ListViewModel, navController: NavHostController,
) {
  when {
    viewModel.confirmDeleteMyitemDialog != null -> {
      DeleteAlertDialog(onDelete = viewModel::deleteMyitem, onCancel = {
        viewModel.confirmDeleteMyitemDialog = null
      })
    }
    viewModel.confirmDeleteMylistDialog -> {
      DeleteAlertDialog(onDelete = {
        // or do i navigateUp
        navController.popBackStack()
        // if I launch here would it prevent NPE
        viewModel.deleteMylist()
      }, onCancel = {
        viewModel.confirmDeleteMylistDialog = false
      })
    }
    viewModel.editMylistTitleDialog != null -> {
      EditDialog("Edit List Title", viewModel.editMylistTitleDialog!!, onUpdate = { newTitle ->
        viewModel.updateMylistTitle(newTitle)
        viewModel.editMylistTitleDialog = null
      }, onCancel = { viewModel.editMylistTitleDialog = null })
    }
    viewModel.editMyitemDialog != null -> {
      EditDialog("Edit Item", viewModel.editMyitemDialog?.myitemText!!, onUpdate = { newText ->
        viewModel.updateMyitemText(newText, viewModel.editMyitemDialog?.myitemId!!)
        viewModel.editMyitemDialog = null
      }, onCancel = { viewModel.editMyitemDialog = null })
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
  dialogTitle: String,
  currentText: String,
  onUpdate: (newMylistTitle: String) -> Unit,
  onCancel: () -> Unit
) {
  var editTextField by rememberSaveable {
    mutableStateOf(currentText)
  }

  AlertDialog(onDismissRequest = onCancel, title = {
    Text(text = dialogTitle)
  }, text = {
    TextField(editTextField, { editTextField = it })
  }, confirmButton = {
    Button(onClick = { onUpdate(editTextField) }) {
      Text("OK")
    }
  }, dismissButton = {
    Button(
      onClick = onCancel
    ) {
      Text("Cancel")
    }
  })
}


@Composable
fun ListScreenTopAppBar(
  viewModel: ListViewModel = hiltViewModel(),
) {
  val appBarTitle: String by viewModel.getAppBarTitle().observeAsState("")
  val onDeleteClick = { viewModel.confirmDeleteMylistDialog = true }

  // MVP edit button click will bring up dialog
  val onEditMylistTitleClick = {
    viewModel.editMylistTitleDialog = appBarTitle
  }
  ListScreenTopAppBarContent(appBarTitle, onEditMylistTitleClick, onDeleteClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenTopAppBarContent(
  appBarTitle: String, onEditMylistTitleClick: () -> Unit, onDeleteClick: () -> Unit
) {
  TopAppBar(title = { Text(appBarTitle) }, actions = {
    IconButton(onClick = onEditMylistTitleClick) {
      Icon(Icons.Filled.Edit, contentDescription = "Edit list title")
    }
    IconButton(onClick = onDeleteClick) {
      Icon(Icons.Filled.Delete, contentDescription = "Delete list")
    }
  })
}