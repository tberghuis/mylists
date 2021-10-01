package xyz.tberghuis.mylists.service

import android.util.Log
import com.jcraft.jsch.*
import java.util.*

class BackupService {
  companion object {
    // should I extract parameters from DB??? probably
    // do that in another function later
    fun uploadDb(user: String, host: String, port: Int, password: String) {
      try {
        val ssh = JSch()
        val session: Session = ssh.getSession(user, host, port)

        val config = Properties()
        config["StrictHostKeyChecking"] = "no"
        session.setConfig(config)
        session.setPassword(password)
        session.connect()
        val channel: Channel = session.openChannel("sftp")
        channel.connect()
        val sftp = channel as ChannelSftp
//    sftp.cd(directory)
        // If you need to display the progress of the upload, read how to do it in the end of the article

        // use the put method , if you are using android remember to remove "file://" and use only the relative path

        //TODO Environment getDataDir???
        sftp.put("/data/data/xyz.tberghuis.mylists/databases/mylists.db", "mylists.db")
        val success = true
        if (success) {
          // The file has been uploaded successfully
          Log.d("xxx", "success")
        } else {
          Log.d("xxx", "fail")
        }
        channel.disconnect()
        session.disconnect()
      } catch (e: JSchException) {
        // can I println in release builds?
        // I should probably Log.e
        println(e.message.toString())
        e.printStackTrace()
      } catch (e: SftpException) {
        println(e.message.toString())
        e.printStackTrace()
      }
    }
  }
}