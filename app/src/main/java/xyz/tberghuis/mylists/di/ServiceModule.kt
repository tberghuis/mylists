package xyz.tberghuis.mylists.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xyz.tberghuis.mylists.service.ImportBackupService
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {


  @Provides
  @Singleton
  fun provideImportBackupService(@ApplicationContext appContext: Context): ImportBackupService {
    return ImportBackupService(appContext)
  }

}