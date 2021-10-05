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
import xyz.tberghuis.mylists.util.initSecureChannel
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
    // https://stackoverflow.com/questions/50914254/room-pragma-query
    val c: Cursor = room.query(SimpleSQLiteQuery("pragma wal_checkpoint(full)"))
    if (c.moveToFirst() && c.getInt(0) == 1) throw RuntimeException("Checkpoint was blocked from completing")
  }

  // should I extract parameters from DB??? probably
  // do that in another function later
  suspend fun uploadDb(
    bs: BackupSettings,
  ): BackupResult {
    withContext(Dispatchers.IO) {
      flushDbWal()
    }

    // this is probably bad programming
    // the correct way will come with time
    // my mantra... do it wrong
    try {
      val channelWrapper = initSecureChannel(bs.user, bs.host, bs.port, bs.password)

      //TODO Environment getDataDir???
      channelWrapper.sftp.put("/data/data/xyz.tberghuis.mylists/databases/mylists.db", bs.filePath)
      channelWrapper.disconnect()
      val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
      val currentDate = sdf.format(Date())
      return BackupResult("success", "", currentDate)
    } catch (e: Exception) {
      return BackupResult("fail", e.message.toString(), "")
    }
  }

}
