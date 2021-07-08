package com.hcy.airadownload

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hcy.airadownload.databinding.ActivityMainBinding
import com.hcy.updata.DownloadRes
import com.hcy.updata.FileUtils
import com.hcy.updata.UpgradeManager
import java.io.File
import java.util.jar.*

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        init()
    }

    var actionTime = 0
    var sb = StringBuilder("")
    var file: File? = null

    @SuppressLint("SetTextI18n")
    private fun init() {
        mBinding.apply {
            tvTitle.text = "下载文件名"
            btnAction.setOnClickListener {
                actionTime++
                if (actionTime % 2 == 1) {
                    log("开始")
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ), 0
                    )
                    val checkSelfPermission = ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    )
                    if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
                        UpgradeManager.downloadApk(
                            "http://192.168.3.199:8071/apk/Salesman_debug_0305_(61)_1614927730692.apk",
                            "${FileUtils.getCacheFile(application).absolutePath}/update.apk",
                            DownloadRes(onSuccsee = { file ->
                                this@MainActivity.file = file
                                log("下载完成File${file.absoluteFile}")
                            },
                                onFail = {
                                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                                    log("失败！${it}")
                                },
                                onProcess = { progress ->
                                    try {
                                        Handler(mainLooper).post {
                                            pbMain.progress = progress
                                            tvPro.text = "${progress}%"
                                            log("progress${progress}%")
                                        }
                                    } catch (e: Exception) {
                                    }
                                })
                        )
                    }
                } else {
                    log("暂停")
                    UpgradeManager.stop()
                }


            }
            btnClear.setOnClickListener {
                file?.run {
                    if (exists()) {
                        delete()
                        log("清除")
                    }
                }
            }
        }


    }

    fun log(msg: String) {
        sb.append("${msg}\n")
        Handler(Looper.getMainLooper()).post {
            mBinding.tvLog.text = sb.toString()
        }
    }
}
