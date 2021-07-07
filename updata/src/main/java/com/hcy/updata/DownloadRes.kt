package com.hcy.updata

import java.io.File

/**
 * @author 侯程月
 * @date 13:57
 * description：
 */
class DownloadRes(
    val onSuccsee: (file: File) -> Unit,
    val onFail: (msg: String) -> Unit,
    val onProcess: (progress: Int) -> Unit
)
