package xyz.tberghuis.mylists.tmp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DemoModule {

  @Provides
  fun provideItemDemoDao(database: DemoDatabase): ItemDemoDao {
    return database.itemDemoDao()
  }


  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext appContext: Context): DemoDatabase {
    return Room.databaseBuilder(
      appContext,
      DemoDatabase::class.java,
      "demo.db"
    ).build()
  }
}
