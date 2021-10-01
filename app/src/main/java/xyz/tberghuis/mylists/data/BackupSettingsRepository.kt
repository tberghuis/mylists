package xyz.tberghuis.mylists.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


data class BackupSettings(
  val user: String,
  val host: String,
  val port: Int,
  val password: String
)

class BackupSettingsRepository(private val dataStore: DataStore<Preferences>) {
  private object PreferencesKeys {
    val BACKUP_USER = stringPreferencesKey("backup_user")
    val BACKUP_HOST = stringPreferencesKey("backup_host")
    val BACKUP_PORT = intPreferencesKey("backup_port")
    val BACKUP_PASSWORD = stringPreferencesKey("backup_password")
  }


  val backupSettingsFlow: Flow<BackupSettings> = dataStore.data
    .catch { exception ->
      // dataStore.data throws an IOException when an error is encountered when reading data
      if (exception is IOException) {
        Log.e("xxx", "Error reading preferences.", exception)
        emit(emptyPreferences())
      } else {
        throw exception
      }
    }.map { preferences ->
      // TODO
      BackupSettings(
        "user", "host", 22, "password"

      )
    }
}