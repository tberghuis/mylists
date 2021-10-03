package xyz.tberghuis.mylists.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val BACKUP_SETTINGS_NAME = "backup_settings"
private val Context.dataStore by preferencesDataStore(
  name = BACKUP_SETTINGS_NAME
)

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {
  @Provides
  @Singleton
  fun provideDatastore(@ApplicationContext appContext: Context): DataStore<Preferences> {
    return appContext.dataStore
  }
}