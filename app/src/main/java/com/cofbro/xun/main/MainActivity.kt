package com.cofbro.xun.main

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cofbro.hymvvm.base.BaseActivity
import com.cofbro.xun.R
import com.cofbro.xun.databinding.ActivityMainBinding
import com.cofbro.xun.view.InputView

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding?.bottomNavigationView!!.setupWithNavController(navHostFragment.navController)
    }
    //使editText点击外部时候失去焦点
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                v?.clearFocus()
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
    }


    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is InputView) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }
}