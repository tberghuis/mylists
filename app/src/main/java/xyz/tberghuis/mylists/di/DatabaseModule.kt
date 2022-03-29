package xyz.tberghuis.mylists.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xyz.tberghuis.mylists.data.AppDatabase
import xyz.tberghuis.mylists.data.MIGRATION_1_2
import xyz.tberghuis.mylists.data.MyitemDao
import xyz.tberghuis.mylists.data.MylistDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

  @Provides
  fun provideMyitemDao(database: AppDatabase): MyitemDao {
    return database.myitemDao()
  }

  @Provides
  fun provideMylistDao(database: AppDatabase): MylistDao {
    return database.mylistDao()
  }

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
    return Room.databaseBuilder(
      appContext,
      AppDatabase::class.java,
      "mylists.db"
    )
      .addMigrations(MIGRATION_1_2)
      .build()
  }
}