package xyz.tberghuis.mylists.tmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import org.burnoutcrew.reorderable.*


class FakeViewModel {
  val list = listOf("item 1", "item 2", "item 3")
}

val fvm = FakeViewModel()

@Composable
fun DragNDropDemo() {
  ReorderableList()
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
