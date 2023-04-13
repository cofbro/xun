package com.cofbro.xun.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.cofbro.hymvvm.base.BaseActivity
import com.cofbro.hymvvm.base.saveUsedSp
import com.cofbro.xun.databinding.ActivityLoginBinding
import com.cofbro.xun.main.MainActivity
import com.hjq.toast.ToastUtils

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        /**
         * 清除输入框的焦点
         */
        binding?.root!!.setOnClickListener {
            it.clearFocus()
        }

        binding?.tvLogin!!.setOnClickListener {
            val username = binding!!.ipUsername.getTextString()
            val password = binding!!.ipPassword.getTextString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(username, password, "登录成功！") {
                    Log.d("chy", "login success: username: ${it.username}, password: ${it.password}")
                    mContext?.apply {
                        saveUsedSp("username", username)
                        saveUsedSp("password", password)
                        saveUsedSp("userId", it.objectId)
                        saveUsedSp("email", it.email)
                    }
                    toMainActivity()
                    finish()
                }
            } else ToastUtils.show("请将信息补充完整！")

        }

        binding?.tvCreateNow!!.setOnClickListener {
            viewModel.toRegister(this)
        }

    }

    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}