package com.cofbro.xun.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentHomeBinding
import com.cofbro.xun.findViews
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ScaleInTransformer

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    private var imageList = ArrayList<Int>()
    private lateinit var group: List<View>
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        initView()
        initEvent()

    }

    private fun initEvent() {
        group[1].setOnClickListener {
            (group[4] as DrawerLayout).openDrawer(GravityCompat.START)
        }
    }

    private fun initView() {
        group = requireActivity().findViews()
        initBannerView()
        group[0].setBackgroundColor(resources.getColor(R.color.statusBarColor, null))
        (group[1] as ImageView).setImageResource(R.drawable.ic_more)
        (group[2] as TextView).text = "主页"
    }

    private fun initBannerView() {
        // data of banner
        imageList = java.util.ArrayList()
        imageList.add(R.drawable.banner)
        imageList.add(R.drawable.banner)
        imageList.add(R.drawable.banner)
        imageList.add(R.drawable.banner)
        binding!!.banner.apply {
            setPageTransformer(ScaleInTransformer())
            setAdapter(ImageAdapter(imageList))
            indicator = CircleIndicator(context)
            start()
        }
    }

}