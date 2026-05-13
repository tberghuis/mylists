package xyz.tberghuis.mylists.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BackupSettingsRepository(
  private val dataStore: DataStore<Preferences>
) {
  private companion object PreferencesKeys {
    val BACKUP_USER = stringPreferencesKey("backup_user")
    val BACKUP_HOST = stringPreferencesKey("backup_host")
    val BACKUP_PORT = intPreferencesKey("backup_port")
    val BACKUP_PASSWORD = stringPreferencesKey("backup_password")
    val BACKUP_FILEPATH = stringPreferencesKey("backup_filepath")
    val BACKUP_LAST_TIME = stringPreferencesKey("backup_last_time")
  }

  val userFlow: Flow<String> = dataStore.data.map { preferences ->
    preferences[BACKUP_USER] ?: ""
  }

  suspend fun updateUser(user: String) {
    dataStore.edit { preferences ->
      preferences[BACKUP_USER] = user
    }
  }

  val hostFlow: Flow<String> = dataStore.data.map { preferences ->
    preferences[BACKUP_HOST] ?: ""
  }

  suspend fun updateHost(host: String) {
    dataStore.edit { preferences ->
      preferences[BACKUP_HOST] = host
    }
  }


  val portFlow: Flow<Int> = dataStore.data.map { preferences ->
    preferences[BACKUP_PORT] ?: 22
  }

  suspend fun updatePort(port: Int) {
    dataStore.edit { preferences ->
      preferences[BACKUP_PORT] = port
    }
  }

  val passwordFlow: Flow<String> = dataStore.data.map { preferences ->
    preferences[BACKUP_PASSWORD] ?: ""
  }

  suspend fun updatePassword(password: String) {
    dataStore.edit { preferences ->
      preferences[BACKUP_PASSWORD] = password
    }
  }

  val filePathFlow: Flow<String> = dataStore.data.map { preferences ->
    preferences[BACKUP_FILEPATH] ?: ""
  }

  suspend fun updateFilePath(filePath: String) {
    dataStore.edit { preferences ->
      preferences[BACKUP_FILEPATH] = filePath
    }
  }

  val lastBackupTimeFlow: Flow<String> = dataStore.data.map { preferences ->
    preferences[BACKUP_LAST_TIME] ?: "N/A"
  }

  suspend fun updateLastBackupTime(lastBackupTime: String) {
    dataStore.edit { preferences ->
      preferences[BACKUP_LAST_TIME] = lastBackupTime
    }
  }
}