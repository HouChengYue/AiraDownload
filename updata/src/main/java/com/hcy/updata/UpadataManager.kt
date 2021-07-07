package com.hcy.updata

import android.app.Application
import android.util.Log
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.download.DownloadEntity
import com.arialyy.aria.core.download.DownloadTaskListener
import com.arialyy.aria.core.task.DownloadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author 侯程月
 * @date 13:48
 * description：检查更新管理类
 */
object UpadataManager {

    /**
     * 下载Apk
     */
    fun downloadApk(apkUrl: String, localPath: String, res: DownloadRes) {
        Holder.instance.apply {
            this.mApkUrl = apkUrl
            this.mLocalPath = localPath
            this.mRes = res
            init()
        }
    }

    /**
     * 停止下载
     */
    fun stop() {
        Holder.instance.stop()
    }

    fun init(app: Application) {
        Holder.instance.initAria(app)
    }


}

class Holder : DownloadTaskListener {
    companion object {
        val instance by lazy { Holder() }
    }

    lateinit var app: Application

    lateinit var mRes: DownloadRes

    lateinit var mApkUrl: String

    lateinit var mLocalPath: String

    var progress = 0

    private var taskId = -1L

    fun initAria(app: Application) {
        this.app=app
        Aria.init(app)
    }

    fun init() {
        Aria.download(this).register()
        val task = Aria.download(this).getFirstDownloadEntity(mApkUrl)
        dealTask(task)
    }

    private fun dealTask(task: DownloadEntity?) {
        GlobalScope.launch {
            with(Dispatchers.IO) {
                val creatNewTask = {
                    File(mLocalPath).apply {
                        delete()
                        createNewFile()
                    }
                    taskId = Aria.download(this@Holder)
                        .load(mApkUrl)
                        .ignoreFilePathOccupy()
                        .setFilePath(mLocalPath, true)
                        .create()
                }
                if (task == null) {
//            检查本地是否有文件
                    val checkRes = FileUtils.checkApkisValid(app, mLocalPath)
                    if (checkRes) {
                        mRes.onSuccsee.invoke(File(mLocalPath))
                    } else {
                        creatNewTask.invoke()
                    }

                } else {
                    task.run {
//                下载完成
                        if (isComplete) {
                            val file = File(filePath)
                            if (!file.exists()) {
                                creatNewTask.invoke()
                            } else {
                                val checkRes = FileUtils.checkApkisValid(app, filePath)
                                if (checkRes) {
                                    GlobalScope.launch {
                                        with(Dispatchers.Main) {
                                            mRes.onSuccsee.invoke(file)
                                        }
                                    }
                                } else {
                                    creatNewTask.invoke()
                                }
                            }
                        } else {
//                 未下载完成
//                  0：失败；1：完成；2：停止；3：等待；
//                  4：正在执行；5：预处理；6：预处理完成；7：取消任务
                            when (state) {
                                0 -> {
//                                    Aria.download(this).load(id).cancel();
                                    creatNewTask.invoke()
                                }
                                2, 3, 5, 6, 7 -> {
                                    taskId = id
                                    Aria.download(this).load(id).resume()
                                }
                                else -> {
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    fun stop() {
        if (taskId != -1L) {
            Aria.download(this).load(taskId).stop()
            Aria.download(this).unRegister()
        }
    }


    override fun onWait(task: DownloadTask?) {
        Log.e(TAG, "onWait: ")
    }

    override fun onPre(task: DownloadTask?) {
        Log.e(TAG, "onPre: ")
    }

    override fun onTaskPre(task: DownloadTask?) {
        Log.e(TAG, "onTaskPre: ")
    }

    override fun onTaskResume(task: DownloadTask?) {
        Log.e(TAG, "onTaskResume: ")
    }

    override fun onTaskStart(task: DownloadTask?) {
        Log.e(TAG, "onTaskStart: ")
    }

    override fun onTaskStop(task: DownloadTask?) {
        Log.e(TAG, "onTaskStop: ")
    }

    override fun onTaskCancel(task: DownloadTask?) {
        Log.e(TAG, "onTaskCancel: ")
    }

    val TAG = "TAG"
    override fun onTaskFail(task: DownloadTask?, e: Exception?) {
        mRes.onFail.invoke(e?.message ?: "下载失败！")
        Log.e(TAG, "onTaskFail: ${e?.message}")
    }

    override fun onTaskComplete(task: DownloadTask?) {
        mRes.onSuccsee.invoke(File(mLocalPath))
    }

    override fun onTaskRunning(task: DownloadTask?) {
        progress = task?.percent ?: 0
        mRes.onProcess.invoke(progress)
        Log.e(TAG, "onTaskRunning: progress${progress}")
    }

    override fun onNoSupportBreakPoint(task: DownloadTask?) {
        Log.e(TAG, "onNoSupportBreakPoint: ")
    }


}
