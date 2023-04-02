package com.cofbro.xun.login

import cn.leancloud.LCUser
import com.cofbro.hymvvm.base.BaseViewModel


class LoginViewModel: BaseViewModel<LoginRepository>() {
    fun login(username: String, password: String, onSuccess: (LCUser) -> Unit) {
        repository.leanCloudUtils.login(username, password, onSuccess)
    }
}