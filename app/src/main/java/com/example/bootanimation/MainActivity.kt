package com.example.bootanimation

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tananaev.adblib.AdbConnection
import com.tananaev.adblib.AdbCrypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.net.Socket


class MainActivity : AppCompatActivity() {
    private val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)!!) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.btn_a)!!.setOnClickListener {
            replaceBootAnimation("bootanimation_a.zip")
        }
        findViewById<Button>(R.id.btn_b)!!.setOnClickListener {
            replaceBootAnimation("bootanimation_b.zip")
        }
    }

    private fun replaceBootAnimation(name: String) {
        val progressDialog = ProgressDialog.show(context, "修改中", "loading", true, false)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val bootanimation = File(cacheDir, name).absolutePath
                copyFileFromAssets(name, bootanimation)

                val socket = Socket("127.0.0.1", 5555)
                val crypto = AdbCrypto.generateAdbKeyPair { data -> Base64.encodeToString(data, Base64.NO_WRAP) }
                val connection = AdbConnection.create(socket, crypto)
                connection.connect()
                connection.open("remount:")
                connection.open("shell:cp $bootanimation /system/media/bootanimation.zip")
                connection.open("shell:chmod 644 /system/media/bootanimation.zip")
                connection.open("shell:chown root:root /system/media/bootanimation.zip")
                runOnUiThread {
                    Toast
                        .makeText(context, "修改完成", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("error")
                        .setMessage("${e.message}")
                        .setPositiveButton("ok") { _, _ ->

                        }
                        .create()
                        .show()
                }
            } finally {
                progressDialog.cancel()
            }
        }
    }


    private fun copyFileFromAssets(assetsFilePath: String, destFilePath: String) {
        assets
            .open(assetsFilePath)
            .use { ins ->
                File(destFilePath)
                    .outputStream()
                    .use { ous ->
                        ins.copyTo(ous)
                    }
            }
    }
}
