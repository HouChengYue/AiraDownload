package com.hcy.upgrade

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import java.io.File

/**
 * @author 侯程月
 * @date 14:14
 * description：
 */
object FileUtils {


    /**
     * 获取apk 文件版本号
     */
    fun getApkInfo(context: Context, apkPath: String): PackageInfo? {
        val pm: PackageManager = context.packageManager
        val info: PackageInfo? =
            pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)
        if (info != null) {
            val appInfo: ApplicationInfo = info.applicationInfo
            val packageName: String = appInfo.packageName //得到安装包名称
            val version: String = info.versionName //获取安装包的版本号
            Log.i("TAG", "getApkIcon: $packageName-------$version")
            return info
        }
        return null
    }

    /**
     * 检查apk 是否有效
     *
     */
    fun checkApkisValid(context: Context,apkPath: String): Boolean {
        return getApkInfo(context, apkPath) != null
    }

    /**
     * 获取缓存文件
     */
    fun getCacheFile(context: Context): File {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return context.externalCacheDir!!
        }
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        } else {
            context.externalCacheDir!!
        }
    }

}
