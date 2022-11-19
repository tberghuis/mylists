package xyz.tberghuis.mylists

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import xyz.tberghuis.mylists.screens.AddListScreen
import xyz.tberghuis.mylists.screens.BackupScreen
import xyz.tberghuis.mylists.screens.HomeScreen
import xyz.tberghuis.mylists.screens.ListScreen
import xyz.tberghuis.mylists.tmp.DragNDropDemo
import xyz.tberghuis.mylists.tmp.migration.MigrationScreen
import xyz.tberghuis.mylists.ui.theme.MyListsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MyListsTheme {
        MyApp()
//        MigrationScreen()
//        DragNDropDemo()
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
      "list/{mylistId}",
      arguments = listOf(
        navArgument("mylistId") { type = NavType.IntType },
      )
    ) { backStackEntry ->
//      val mylistId: Int = backStackEntry.arguments?.getInt("mylistId")!!
      ListScreen(
        navController = navController,
      )
    }
  }
}