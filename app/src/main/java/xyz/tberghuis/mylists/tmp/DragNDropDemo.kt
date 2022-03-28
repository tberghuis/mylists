package xyz.tberghuis.mylists.tmp

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.*
import xyz.tberghuis.mylists.util.logd
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlin.math.max
import kotlin.math.min


@HiltViewModel
class DemoViewModel @Inject constructor(
  val itemDemoDao: ItemDemoDao
) : ViewModel() {
//  val list = listOf("item 1", "item 2", "item 3")

  init {
    // insert demo data

    val item1 = ItemDemo(itemText = "item 1", itemOrder = 0)
    val item2 = ItemDemo(itemText = "item 2", itemOrder = 1)
    val item3 = ItemDemo(itemText = "item 3", itemOrder = 2)

    viewModelScope.launch {

      itemDemoDao.deleteAll()

      itemDemoDao.insertAll(item1, item2, item3)
    }
  }

  // doitwrong, should use vararg or list and update in a transaction
  fun update(itemDemo: ItemDemo) {
    viewModelScope.launch {
      itemDemoDao.update(itemDemo)
    }
  }
}

//val fvm = FakeViewModel()

@Composable
fun DragNDropDemo() {
  RenderItemDemoList()
//  ReorderableList()
}

//fun <T> stateSaver() = Saver<MutableState<T>, Any>(
//    save = { state -> state.value ?: "null" },
//    restore = { value ->
//        @Suppress("UNCHECKED_CAST")
//        mutableStateOf((if (value == "null") null else value) as T)
//    }
//)

@Composable
fun <T : Parcelable> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
  return rememberSaveable(
    saver = listSaver(
      save = { it.toList() },
      restore = { it.toMutableStateList() }
    )
  ) {
    elements.toList().toMutableStateList()
  }
}


@Composable
fun RenderItemDemoList() {
  val vm: DemoViewModel = hiltViewModel()
//  val items = vm.itemDemoDao.getAll().collectAsState(initial = listOf())

  val state = rememberReorderState()

//  val orderableList = remember {
//    listOf<ItemDemo>().toMutableStateList()
//
////    mutableStateListOf()
//  }

  val orderableList = rememberMutableStateListOf<ItemDemo>()


  LazyColumn(
    state = state.listState,
    modifier = Modifier.reorderable(
      state, { from, to ->
//      move(orderableList, from.index, to.index, vm::update)
        orderableList.move(from.index, to.index)
        // fire event order changed
      },
      onDragEnd = { startIndex: Int, endIndex: Int ->
        onDragEnd(orderableList, startIndex, endIndex, vm)
      }
    )
  ) {
    items(orderableList, { it.itemId }) { item ->
      Card(
        elevation = 2.dp,
        modifier = Modifier
          .padding(10.dp)
          .fillMaxWidth()
          .draggedItem(state.offsetByKey(item.itemId))
          .detectReorderAfterLongPress(state)
      ) {

        Text("${item.itemText} order ${item.itemOrder}")
      }

    }
  }


  LaunchedEffect(Unit) {
    vm.itemDemoDao.getAll().collect {
      orderableList.clear()
      orderableList.addAll(it)
    }
  }

//  LaunchedEffect(Unit) {
//    snapshotFlow { orderableList.toList() }
//      .collect {
//        logd("orderableList $orderableList")
//      }
//  }


}

fun onDragEnd(
  itemDemoList: List<ItemDemo>,
  startIndex: Int,
  endIndex: Int,
  demoViewModel: DemoViewModel
) {
  logd("onDragEnd startIndex $startIndex endIndex $endIndex")

  if (startIndex == endIndex) {
    return
  }

  // is there a more elegant way???
  for (i in min(startIndex, endIndex)..max(startIndex, endIndex)) {
    // doitwrong, should do at once * flatten list to vararg
    demoViewModel.update(itemDemoList[i].copy(itemOrder = i))
  }

  // lets do a hardcode...
  // item 1 to the bottom
//  val item1 = ItemDemo(itemText = "item 1", itemOrder = 2)
//  val item2 = ItemDemo(itemText = "item 2", itemOrder = 0)
//  val item3 = ItemDemo(itemText = "item 3", itemOrder = 1)
//
//  demoViewModel
}


fun move(
  list: MutableList<ItemDemo>,
  fromIdx: Int,
  toIdx: Int,
  update: (itemDemo: ItemDemo) -> Unit
) {
  logd("fromIdx ${fromIdx} toIdx ${toIdx}")

  if (fromIdx == toIdx) {
    return
  }
  // doitwrong
  // should run updates as a transaction

  // i do really want to update all changed at once

  update(list[fromIdx].copy(itemOrder = toIdx))

  when {
    toIdx > fromIdx -> {
      for (i in fromIdx + 1..toIdx) {
        update(list[i].copy(itemOrder = list[i].itemOrder - 1))
      }
    }
    else -> {
      // todo
//      for (i in fromIdx downTo toIdx + 1) {
//
//      }
    }
  }

}


@Composable
fun ReorderableList() {
  val state = rememberReorderState()
  val data = List(100) { "item $it" }.toMutableStateList()
  LazyColumn(
    state = state.listState,
    modifier = Modifier.reorderable(state, { from, to -> data.move(from.index, to.index) })
  ) {
    items(data, { it }) { item ->
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .draggedItem(state.offsetByKey(item))
          .detectReorderAfterLongPress(state)
      ) {
        Text(text = item)
      }
    }
  }
}
