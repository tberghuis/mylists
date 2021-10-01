package xyz.tberghuis.mylists.tmp

import android.os.Environment
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.jcraft.jsch.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

import java.util.*


@Composable
fun BackupDbSandbox() {
  ButtonExample()
}


@Composable
fun ButtonExample() {

  val scope = rememberCoroutineScope()
  var host by remember { mutableStateOf("") }
  var user by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  Column {
    TextField(
      value = host,
      onValueChange = { host = it },
      label = { Text("host") }
    )
    TextField(
      value = user,
      onValueChange = { user = it },
      label = { Text("user") }
    )
    TextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("password") }
    )


    Button(
      onClick = {

//      val currentDBPath = "//data//xyz.tberghuis.mylists//databases//mylists.db";
//
//      val currentDB = File(currentDBPath);
//
//      val data = Environment.getDataDirectory()
//      Log.d("xxx", currentDB.toString())


        scope.launch(Dispatchers.Default) {
          jschTest(host, user, password)
        }


      }
    ) {
      Text("Button")
    }
  }


}

fun jschTest(host: String, user: String, password: String) {
  try {
    val ssh = JSch()
    val session: Session = ssh.getSession(user, host, 22)

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
    sftp.put("/data/data/xyz.tberghuis.mylists/databases/mylists.db", "mylists.db")
    val success = true
    if (success) {
      // The file has been uploaded succesfully
      Log.d("xxx", "success")
    } else {
      Log.d("xxx", "fail")
    }
    channel.disconnect()
    session.disconnect()
  } catch (e: JSchException) {
    println(e.message.toString())
    e.printStackTrace()
//    Log.e
  } catch (e: SftpException) {
    println(e.message.toString())
    e.printStackTrace()
  }
}