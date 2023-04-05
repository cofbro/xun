package com.cofbro.xun.home

import android.os.Bundle
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentHomeBinding
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ScaleInTransformer

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    private var imageList = ArrayList<Int>()
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        initBannerView()
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