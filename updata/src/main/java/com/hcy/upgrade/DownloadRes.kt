package com.hcy.upgrade

import com.arialyy.aria.core.download.DownloadEntity
import com.arialyy.aria.core.task.DownloadTask
import java.io.File

/**
 * @author 侯程月
 * @date 13:57
 * description：
 */
class DownloadRes(
    val onSuccsee: (file: File) -> Unit,
    val onFail: (msg: String) -> Unit,
    val onProcess: (progress: Int,task: DownloadTask?) -> Unit
)
