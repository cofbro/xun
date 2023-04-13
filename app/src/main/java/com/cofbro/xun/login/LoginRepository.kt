package com.cofbro.xun.login

import cn.leancloud.LCUser
import com.cofbro.hymvvm.base.BaseRepository

class LoginRepository : BaseRepository() {
    fun login(username: String, password: String, msg: String, onSuccess: (LCUser) -> Unit) {
        executeLCRequest {
            leanCloudUtils.login(username, password, msg, onSuccess)
        }
    }
}