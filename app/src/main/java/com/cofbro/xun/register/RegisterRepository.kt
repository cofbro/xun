package com.cofbro.xun.register

import cn.leancloud.LCUser
import com.cofbro.hymvvm.base.BaseRepository

class RegisterRepository : BaseRepository() {
    fun register(
        username: String,
        password: String,
        email: String,
        ifTeacher: Boolean,
        msg: String,
        onSuccess: (LCUser) -> Unit
    ) {
        leanCloudUtils.register(username, password, email, ifTeacher, msg, onSuccess)
    }
}