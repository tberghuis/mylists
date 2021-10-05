package xyz.tberghuis.mylists.service

import android.content.Context
import android.util.Log
import com.jcraft.jsch.*
import xyz.tberghuis.mylists.data.BackupSettings
import xyz.tberghuis.mylists.util.initSecureChannel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImportBackupService
@Inject constructor(
  private val context: Context
) {

  fun import(
    bs: BackupSettings
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

    } catch (e: JSchException) {

    } catch (e: SftpException) {

    }


  }

}