package com.hcy.airadownload

import android.app.Application
import com.hcy.upgrade.UpgradeManager

/**
 * @author 侯程月
 * @date 16:27
 * description：
 */
class tApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        UpgradeManager.init(this)
    }

}
