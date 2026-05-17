package xyz.tberghuis.mylists.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import xyz.tberghuis.mylists.data.BackupSettingsRepository
import xyz.tberghuis.mylists.screens.AddListViewModel
import xyz.tberghuis.mylists.screens.HomeViewModel
import xyz.tberghuis.mylists.screens.ListViewModel
import xyz.tberghuis.mylists.screens.XxxBackupViewModel
import xyz.tberghuis.mylists.service.BackupService
import xyz.tberghuis.mylists.service.ImportBackupService

val appModule = module {
  singleOf(::provideDataBase) // AppDatabase
  singleOf(::provideMyitemDao) // MyitemDao
  singleOf(::provideMylistDao) // MylistDao
  singleOf(::provideDatastore) // DataStore<Preferences> 

  viewModelOf(::HomeViewModel)

  viewModelOf(::AddListViewModel)

  singleOf(::BackupSettingsRepository) // DataStore<Preferences> 
  singleOf(::ImportBackupService) // DataStore<Preferences> 
  singleOf(::BackupService) // DataStore<Preferences> 
  viewModelOf(::XxxBackupViewModel)
//  viewModelOf(::BackupViewModel)

  viewModelOf(::ListViewModel)
}