package xyz.tberghuis.mylists.service

import android.content.Context
import android.util.Log
import com.jcraft.jsch.*
import xyz.tberghuis.mylists.data.BackupSettings
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
      val ssh = JSch()
      val session: Session = ssh.getSession(bs.user, bs.host, bs.port)
      val config = Properties()
      // do i need this???
      config["StrictHostKeyChecking"] = "no"
      session.setConfig(config)
      session.setPassword(bs.password)
      session.connect()
      val channel: Channel = session.openChannel("sftp")
      channel.connect()
      val sftp = channel as ChannelSftp

      sftp.get(bs.filePath, "/data/data/xyz.tberghuis.mylists/databases/mylists.db")

      channel.disconnect()
      session.disconnect()

    } catch (e: JSchException) {

    } catch (e: SftpException) {

    }


  }

}