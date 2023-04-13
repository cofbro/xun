package com.cofbro.xun.main

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.cofbro.hymvvm.base.BaseActivity
import com.cofbro.xun.R
import com.cofbro.xun.databinding.ActivityMainBinding
import com.cofbro.xun.view.InputView
import com.hjq.toast.ToastUtils

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private lateinit var bookMenuItem: MenuItem
    private lateinit var controller: NavController
    private val BACK_PRESSED_INTERVAL = 2000
    private var currentBackPressedTime = 0L
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initView()
        initEvent()
    }

    private fun initView() {
        // 设置底部导航栏
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding?.bottomNavigationView!!.setupWithNavController(navHostFragment.navController)
        controller = navHostFragment.navController
        bookMenuItem = binding!!.bottomNavigationView.menu.findItem(R.id.bookFragment)
        // 设置侧边栏全屏显示
        setFullScreen()
    }

    private fun initEvent() {
        // menu关闭按钮
        binding!!.ivMenuClose.setOnClickListener {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        }
        // 预约按钮
        binding!!.tvMenuBookNow.setOnClickListener {
            onNavDestinationSelected(bookMenuItem, controller)
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    // 使editText点击外部时候失去焦点
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                v?.clearFocus()
                if (v is EditText) hideInputMethod()
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
    }


    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is InputView || v is EditText)) {
            val leftTop = intArrayOf(0, 0)
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    override fun onBackPressed() {
        val count = controller.backQueue.size
        if (count == 2 && System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis()
            ToastUtils.show("再按一次退出")
            return
        }
        super.onBackPressed()
    }

    private fun setFullScreen() {
        val params = binding!!.csMainMenu.layoutParams
        params.width = resources.displayMetrics.widthPixels
        binding!!.csMainMenu.layoutParams = params
    }

    private fun hideInputMethod() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)

    }


}