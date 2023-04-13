package com.cofbro.xun.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.cofbro.hymvvm.base.*
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentProfileBinding
import com.cofbro.xun.findViews

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {
    private lateinit var group: List<View>
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        initView()
        initEvent()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        // 导航栏
        group = requireActivity().findViews()
        group[0].setBackgroundColor(resources.getColor(R.color.statusBarColor, null))
        (group[1] as ImageView).setImageResource(R.drawable.ic_more)
        (group[2] as TextView).text = "我的信息"
        // 用户信息
        val username = mContext?.getBySp(SP_USER_NAME)
        val id = mContext?.getBySp(SP_USER_ID)
        val email = mContext?.getBySp(SP_EMAIL)
        binding!!.tvProfileUsername.text = username
        binding!!.tvProfileId.text = "Member ID:$id"
        binding!!.tvProfileEmail.text = email
    }

    private fun initEvent() {
        // 导航栏
        group[1].setOnClickListener {
            (group[4] as DrawerLayout).openDrawer(GravityCompat.START)
        }

        // 我的预约
        binding!!.csProfileMyBooking.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_bookRecordFragment)
        }
    }
}