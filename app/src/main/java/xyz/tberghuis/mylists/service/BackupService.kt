package xyz.tberghuis.mylists.service

import android.content.Context
import android.database.Cursor
import xyz.tberghuis.mylists.data.BackupSettings
import java.text.SimpleDateFormat
import java.util.*
import androidx.sqlite.db.SimpleSQLiteQuery
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
  private val room: AppDatabase,
  @ApplicationContext private val context: Context
) {
  private fun flushDbWal() {
    // https://stackoverflow.com/questions/50914254/room-pragma-query
    val c: Cursor = room.query(SimpleSQLiteQuery("pragma wal_checkpoint(full)"))
    if (c.moveToFirst() && c.getInt(0) == 1) throw RuntimeException("Checkpoint was blocked from completing")
  }

  // TODO proper testing
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
      val dbPath = context.getDatabasePath("mylists.db").absolutePath
      // todo if path don't exist throw....

      channelWrapper.sftp.put(dbPath, bs.filePath)
      channelWrapper.disconnect()
      val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
      val currentDate = sdf.format(Date())
      return BackupResult("success", "", currentDate)
    } catch (e: Exception) {
      return BackupResult("fail", e.message.toString(), "")
    }
  }
}