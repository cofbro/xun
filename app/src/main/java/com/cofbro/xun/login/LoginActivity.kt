package com.cofbro.xun.login


import android.os.Bundle
import android.util.Log
import com.cofbro.hymvvm.base.BaseActivity
import com.cofbro.xun.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        /**
         * InputView有缺陷，在这里清除输入框的焦点
         */
        binding?.root!!.setOnClickListener {
            it.clearFocus()
        }


        binding?.tvLogin!!.setOnClickListener {
            val username = binding!!.ipUsername.getTextString()
            val password = binding!!.ipPassword.getTextString()
            viewModel.login(username, password) {
                Log.d("chy", "login success: username: ${it.username}, password: ${it.password}")
            }
        }

    }


}