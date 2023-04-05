package com.cofbro.xun.register

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.cofbro.hymvvm.base.BaseActivity
import com.cofbro.xun.R
import com.cofbro.xun.databinding.ActivityRegisterBinding
import com.hjq.toast.ToastUtils

class RegisterActivity : BaseActivity<RegisterViewModel, ActivityRegisterBinding>() {
    private var currentIdentity: ConstraintLayout? = null
    private var ifTeacher: Boolean? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding?.root!!.setOnClickListener {
            it.clearFocus()
        }

        binding?.clTeacher!!.setOnClickListener {
            selectIdentity(it as ConstraintLayout)
        }

        binding?.clStudent!!.setOnClickListener {
            selectIdentity(it as ConstraintLayout)
        }

        binding?.tvRegister!!.setOnClickListener {
            if (ifTeacher == null) ToastUtils.show("请选择您的身份")
            else {
                val username = binding?.ipUsername!!.getTextString()
                val password = binding?.ipPassword!!.getTextString()
                val email = binding?.ipEmailAddress!!.getTextString()
                if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                    viewModel.register(username, password, email, ifTeacher!!, "请注意邮箱点击验证") {
                        // 前往主页
                    }
                } else {
                    ToastUtils.show("请将信息补充完整！")
                }
            }
        }

        binding?.tvCancle!!.setOnClickListener {
            finish()
        }
    }

    private fun selectIdentity(view: ConstraintLayout) {
        currentIdentity?.background = null
        currentIdentity = view
        ifTeacher = view.id == R.id.cl_teacher
        view.background = resources.getDrawable(R.drawable.select_border_bg, null)
    }

}