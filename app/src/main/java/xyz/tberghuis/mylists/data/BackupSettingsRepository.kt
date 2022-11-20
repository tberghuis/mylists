package xyz.tberghuis.mylists.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class BackupSettings(
  val user: String = "",
  val host: String = "",
  val port: Int = 22,
  val password: String = "",
  val filePath: String = "",
  val lastBackupTime: String = "N/A"
)

@Singleton
class BackupSettingsRepository
@Inject constructor(
  private val dataStore: DataStore<Preferences>
) {

  private object PreferencesKeys {
    val BACKUP_USER = stringPreferencesKey("backup_user")
    val BACKUP_HOST = stringPreferencesKey("backup_host")
    val BACKUP_PORT = intPreferencesKey("backup_port")
    val BACKUP_PASSWORD = stringPreferencesKey("backup_password")
    val BACKUP_FILEPATH = stringPreferencesKey("backup_filepath")
    val BACKUP_LAST_TIME = stringPreferencesKey("backup_last_time")
  }

  val backupSettingsFlow: Flow<BackupSettings> = dataStore.data
    .catch { exception ->
      // dataStore.data throws an IOException when an error is encountered when reading data
      if (exception is IOException) {
        Log.e("xxx", "Error reading preferences.", exception)
        // probably better to crash
        emit(emptyPreferences())
      } else {
        throw exception
      }
    }.map { preferences ->
      val user = preferences[PreferencesKeys.BACKUP_USER] ?: ""
      val host = preferences[PreferencesKeys.BACKUP_HOST] ?: ""
      val port = preferences[PreferencesKeys.BACKUP_PORT] ?: 22
      val password = preferences[PreferencesKeys.BACKUP_PASSWORD] ?: ""
      val filePath = preferences[PreferencesKeys.BACKUP_FILEPATH] ?: ""
      val lastBackupTime = preferences[PreferencesKeys.BACKUP_LAST_TIME] ?: "N/A"

      BackupSettings(
        user, host, port, password, filePath, lastBackupTime
      )
    }

  suspend fun save(backupSettings: BackupSettings) {
    dataStore.edit { preferences ->
      preferences[PreferencesKeys.BACKUP_USER] = backupSettings.user
      preferences[PreferencesKeys.BACKUP_HOST] = backupSettings.host
      preferences[PreferencesKeys.BACKUP_PORT] = backupSettings.port
      preferences[PreferencesKeys.BACKUP_PASSWORD] = backupSettings.password
      preferences[PreferencesKeys.BACKUP_FILEPATH] = backupSettings.filePath
      preferences[PreferencesKeys.BACKUP_LAST_TIME] = backupSettings.lastBackupTime
    }
  }

  suspend fun saveBackupTime(time: String) {
    dataStore.edit { preferences ->
      preferences[PreferencesKeys.BACKUP_LAST_TIME] = time
    }
  }
}