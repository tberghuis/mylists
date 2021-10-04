package xyz.tberghuis.mylists.service

import android.content.Context
import android.util.Log
import com.jcraft.jsch.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImportBackupService
@Inject constructor(
  private val context: Context
) {

  fun import(
    user: String,
    host: String,
    port: Int,
    password: String,
    filePath: String
  ) {
    Log.d("xxx", "import service")
    context.deleteDatabase("mylists.db")

    // download db

    //    copy to correct dir

    // restart activity

    try {
      val ssh = JSch()
      val session: Session = ssh.getSession(user, host, port)
      val config = Properties()
      // do i need this???
      config["StrictHostKeyChecking"] = "no"
      session.setConfig(config)
      session.setPassword(password)
      session.connect()
      val channel: Channel = session.openChannel("sftp")
      channel.connect()
      val sftp = channel as ChannelSftp

      sftp.get(filePath, "/data/data/xyz.tberghuis.mylists/databases/mylists.db")

      channel.disconnect()
      session.disconnect()

    } catch (e: JSchException) {

    } catch (e: SftpException) {

    }


  }

}