package xyz.tberghuis.mylists.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val BACKUP_SETTINGS_NAME = "backup_settings"
private val Context.dataStore by preferencesDataStore(
  name = BACKUP_SETTINGS_NAME
)

fun provideDatastore(appContext: Context): DataStore<Preferences> {
  return appContext.dataStore
}