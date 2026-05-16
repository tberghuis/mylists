package xyz.tberghuis.mylists.di

import android.content.Context
import androidx.room.Room
import xyz.tberghuis.mylists.DB_FILENAME
import xyz.tberghuis.mylists.data.AppDatabase
import xyz.tberghuis.mylists.data.MyitemDao
import xyz.tberghuis.mylists.data.MylistDao

fun provideDataBase(context: Context): AppDatabase {
  return Room.databaseBuilder(
    context.applicationContext,
    AppDatabase::class.java,
    DB_FILENAME
  )
//    .addMigrations(MIGRATION_1_2)
    .build()
}

fun provideMyitemDao(database: AppDatabase): MyitemDao {
  return database.myitemDao()
}

fun provideMylistDao(database: AppDatabase): MylistDao {
  return database.mylistDao()
}