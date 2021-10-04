package xyz.tberghuis.mylists.service

import android.database.Cursor
import android.util.Log
import com.jcraft.jsch.*
import xyz.tberghuis.mylists.data.BackupSettings
import java.text.SimpleDateFormat
import java.util.*
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import xyz.tberghuis.mylists.data.AppDatabase
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Singleton


// i need to learn better practices
// this will come with time
class BackupResult(
  val status: String,
  val message: String,
  val time: String
)


@Singleton
class BackupService
@Inject constructor(
  private val room: AppDatabase
) {


  private fun flushDbWal() {
    val c: Cursor = room.query(SimpleSQLiteQuery("pragma wal_checkpoint(full)"))
    if (c.moveToFirst() && c.getInt(0) == 1) throw RuntimeException("Checkpoint was blocked from completing")
  }

  // should I extract parameters from DB??? probably
  // do that in another function later
  suspend fun uploadDb(
    bs: BackupSettings,
//    scope: CoroutineScope
  ): BackupResult {

    // here i want to force flush wal file
//      db.execSQL("pragma wal_checkpoint;", null);


    // async IO flushWal
//    scope.async(Dispatchers.IO) {
//      flushDbWal()
//    }.await()

    withContext(Dispatchers.IO) {
      flushDbWal()
    }


    try {
      val ssh = JSch()
      val session: Session = ssh.getSession(bs.user, bs.host, bs.port)

      val config = Properties()
      config["StrictHostKeyChecking"] = "no"
      session.setConfig(config)
      session.setPassword(bs.password)
      session.connect()
      val channel: Channel = session.openChannel("sftp")
      channel.connect()
      val sftp = channel as ChannelSftp
//    sftp.cd(directory)
      // If you need to display the progress of the upload, read how to do it in the end of the article

      // use the put method , if you are using android remember to remove "file://" and use only the relative path

      //TODO Environment getDataDir???
      sftp.put("/data/data/xyz.tberghuis.mylists/databases/mylists.db", bs.filePath)

      channel.disconnect()
      session.disconnect()

      val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
      val currentDate = sdf.format(Date())

      return BackupResult("success", "", currentDate)


    } catch (e: JSchException) {
      // can I println in release builds?
      // I should probably Log.e
//        println(e.message.toString())
//        e.printStackTrace()
      return BackupResult("fail", e.message.toString(), "")
    } catch (e: SftpException) {
//        println(e.message.toString())
//        e.printStackTrace()
      return BackupResult("fail", e.message.toString(), "")
    }
  }


//    fun importDb() {
//      Log.d("xxx", "import db")
//
//      context.deleteDatabase("mylists.db")
//
//    }


}
