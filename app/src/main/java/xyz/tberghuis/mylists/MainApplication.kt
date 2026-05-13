package xyz.tberghuis.mylists

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import xyz.tberghuis.mylists.di.appModule

class MainApplication : Application() {


  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@MainApplication)
      modules(appModule)
    }
  }
}
