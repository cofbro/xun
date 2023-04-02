package com.cofbro.xun

import android.app.Application
import cn.leancloud.LeanCloud
import com.cofbro.hymvvm.lean.LeanCloudUtils
import com.hjq.toast.ToastUtils

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LeanCloud.initialize(
            this,
            "EWuHigFkg9FMcRPaGiQJariV-gzGzoHsz",
            "1PSNGNmLVIvZwBMGR05cNPU3",
            "https://ewuhigfk.lc-cn-n1-shared.com"
        )
        ToastUtils.init(this)
        LeanCloudUtils.Companion.init(true)
    }
}