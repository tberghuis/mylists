package xyz.tberghuis.mylists.util

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import java.util.*

// probably a better pattern than this
class ChannelWrapper(val sftp: ChannelSftp, private val session: Session) {
  fun disconnect() {
    sftp.disconnect()
    session.disconnect()
  }
}

fun initSecureChannel(user: String, host: String, port: Int, password: String): ChannelWrapper {
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
  return ChannelWrapper(sftp, session)
}
