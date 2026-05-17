package xyz.tberghuis.mylists

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import xyz.tberghuis.mylists.ui.theme.MyListsTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import xyz.tberghuis.mylists.screens.AddListScreen
import xyz.tberghuis.mylists.screens.BackupScreen
import xyz.tberghuis.mylists.screens.HomeScreen
import xyz.tberghuis.mylists.screens.ListScreen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyListsTheme {

        MyApp()

      }
    }
  }
}

@Composable
fun MyApp() {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = "home") {
    // todo use hilt or CompositionLocal to avoid props drilling
    // todo refactor screen and viewmodel names to match with sqlite...
    composable("home") { HomeScreen(navController) }
    composable("backup") { BackupScreen() }
    composable("add-list") { AddListScreen(navController = navController) }
    composable(
      "list/{mylistId}", arguments = listOf(
        navArgument("mylistId") { type = NavType.IntType },
      )
    ) {
      ListScreen(
        navController = navController,
      )
    }
  }
}