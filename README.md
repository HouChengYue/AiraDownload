# AiraDownload
[![](https://jitpack.io/v/HouChengYue/AiraDownload.svg)](https://jitpack.io/#HouChengYue/AiraDownload)
# 用于下载apk安装包的插件
## 添加
   Step 1. Add the JitPack repository to your build file
   Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.HouChengYue:AiraDownload:Tag'
	}
## 使用
### 第一步：在Application 中 初始化
~~~
override fun onCreate() {
        super.onCreate()
        UpadataManager.init(this)
    }
~~~
### 第二步：在要用的地方检查权限
~~~
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
~~~
### 第三步：开始下载/继续下载
~~~
    /**
     * 下载Apk
     * @param apkUrl 包网络地址
     * @param localPath 本地缓存地址
     * @param res 下载结果 @link DownloadRes
     */
   UpadataManager.downloadApk(apkUrl: String, localPath: String, res: DownloadRes)
   UpadataManager.stop() 暂停下载 UpadataManager.downloadApk可以继续下载
 ~~~
