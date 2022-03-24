package xyz.tberghuis.mylists.tmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.*
import javax.inject.Inject


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
      itemDemoDao.insertAll(item1, item2, item3)
    }
  }
}

//val fvm = FakeViewModel()

@Composable
fun DragNDropDemo() {
  ReorderableList()
}

@Composable
fun RenderItemDemoList() {
  val vm: DemoViewModel = hiltViewModel()
  val items = vm.itemDemoDao.getAll().collectAsState(initial = listOf())

  // todo lazycolumn

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
