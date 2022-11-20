package xyz.tberghuis.mylists.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColors = darkColors(
  primary = Purple200,
  primaryVariant = Purple700,
  secondary = Teal200
)

private val LightColors = lightColors(
  primary = Purple500,
  primaryVariant = Purple700,
  secondary = Teal200
)


@Composable
fun MyListsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

  val colors = if (darkTheme) {
    DarkColors
  } else {
    LightColors
  }

  MaterialTheme(colors = colors) {
    content()
  }
}