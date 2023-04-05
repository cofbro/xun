package com.cofbro.xun.login

import android.content.Intent
import cn.leancloud.LCUser
import com.cofbro.hymvvm.base.BaseViewModel
import com.cofbro.xun.register.RegisterActivity


class LoginViewModel : BaseViewModel<LoginRepository>() {
    fun login(
        username: String, password: String, msg: String, onSuccess: (LCUser) -> Unit
    ) {
        repository.leanCloudUtils.login(username, password, msg, onSuccess)
    }

    fun toRegister(activity: LoginActivity) {
        val intent = Intent(activity, RegisterActivity::class.java)
        activity.startActivity(intent)
    }
}