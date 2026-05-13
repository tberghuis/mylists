package xyz.tberghuis.mylists.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import xyz.tberghuis.mylists.MainActivity
import xyz.tberghuis.mylists.util.initSecureChannel

class ImportBackupService
constructor(
  private val context: Context // this is appContext
) {
  // TODO I should do some warning dialog...
  fun import(
    user: String,
    host: String,
    port: Int,
    password: String,
    filePath: String,
    activity: Activity
  ) {
    context.deleteDatabase("mylists.db")
    try {
      val channelWrapper = initSecureChannel(user, host, port, password)
      val dbPath = context.getDatabasePath("mylists.db").absolutePath
      channelWrapper.sftp.get(filePath, dbPath)
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