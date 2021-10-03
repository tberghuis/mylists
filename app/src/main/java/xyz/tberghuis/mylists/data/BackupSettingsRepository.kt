package xyz.tberghuis.mylists.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


data class BackupSettings(
  val user: String,
  val host: String,
  val port: Int,
  val password: String
)


@Singleton
class BackupSettingsRepository
@Inject constructor(
  val dataStore: DataStore<Preferences>
) {


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

      val user = preferences[PreferencesKeys.BACKUP_USER] ?: ""

      BackupSettings(
        user, "host", 22, "password"

      )
    }


  suspend fun testWriteDataStore(backupSettings: BackupSettings) {
    dataStore.edit { preferences ->
      Log.d("xxx", "datastore edit")
      preferences[PreferencesKeys.BACKUP_USER] = backupSettings.user
//        ...
    }
  }
}