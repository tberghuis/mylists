package xyz.tberghuis.mylists.service

import android.database.Cursor
import androidx.sqlite.db.SimpleSQLiteQuery
import xyz.tberghuis.mylists.data.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmpFlushService
@Inject constructor(
  private val room: AppDatabase
) {

  fun flushDbWal() {
    val c: Cursor = room.query(SimpleSQLiteQuery("pragma wal_checkpoint(full)"))
    if (c.moveToFirst() && c.getInt(0) == 1) throw RuntimeException("Checkpoint was blocked from completing")
  }
}