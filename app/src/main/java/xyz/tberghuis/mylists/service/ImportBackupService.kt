package xyz.tberghuis.mylists.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.jcraft.jsch.*
import xyz.tberghuis.mylists.MainActivity
import xyz.tberghuis.mylists.data.BackupSettings
import xyz.tberghuis.mylists.util.initSecureChannel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


// can hilt inject activity???

@Singleton
class ImportBackupService
@Inject constructor(
  private val context: Context // this is appContext
) {

  // I should do some warnings...
  fun import(
    bs: BackupSettings,
    activity: Activity
  ) {
    Log.d("xxx", "import service")
    context.deleteDatabase("mylists.db")

    // download db

    //    copy to correct dir

    // restart activity

    try {
      val channelWrapper = initSecureChannel(bs.user, bs.host, bs.port, bs.password)
      channelWrapper.sftp.get(bs.filePath, "/data/data/xyz.tberghuis.mylists/databases/mylists.db")
      channelWrapper.disconnect()

    } catch (e: Exception) {
// TODO
    }

    triggerRestart(activity)

  }

}

// this is because I don't know how to reinitialise room after import
// https://stackoverflow.com/questions/6609414/how-do-i-programmatically-restart-an-android-app?answertab=active#tab-top
fun triggerRestart(activity: Activity) {
  val intent = Intent(activity, MainActivity::class.java)
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  activity.startActivity(intent)
  activity.finish()
  Runtime.getRuntime().exit(0)
}