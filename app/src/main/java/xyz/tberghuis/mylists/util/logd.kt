package xyz.tberghuis.mylists.util

import android.util.Log
import xyz.tberghuis.mylists.BuildConfig

fun logd(s: String) {
  if (BuildConfig.DEBUG) Log.d("xxx", s)
}