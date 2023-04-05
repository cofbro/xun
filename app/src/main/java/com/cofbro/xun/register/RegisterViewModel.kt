package com.cofbro.xun.register

import cn.leancloud.LCUser
import com.cofbro.hymvvm.base.BaseViewModel

class RegisterViewModel : BaseViewModel<RegisterRepository>() {
    fun register(
        username: String,
        password: String,
        email: String,
        ifTeacher: Boolean,
        msg: String,
        onSuccess: (LCUser) -> Unit
    ) {
        repository.register(username, password, email, ifTeacher, msg, onSuccess)
    }

}