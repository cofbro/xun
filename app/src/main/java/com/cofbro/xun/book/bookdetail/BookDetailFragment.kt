package com.cofbro.xun.book.bookdetail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.hymvvm.base.getBySp
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.BOOKING_INFO_CLASS
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.DATE
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.LOCATION
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.PHONE_NUMBER
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.REMAIN_TIME
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.STUDENT_ID
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.STUDENT_NAME
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.TEACHER_NAME
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.TIME
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.TYPE
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentBookDetailBinding
import com.cofbro.xun.findViews
import com.google.android.material.tabs.TabLayout

class BookDetailFragment : BaseFragment<BookDetailViewModel, FragmentBookDetailBinding>() {
    private lateinit var group: List<View>
    private lateinit var currentDurationView: View
    private var type: String = "情感问题"
    private val weekArray = arrayOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
    private val data by navArgs<BookDetailFragmentArgs>()
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        initView()
        initEvent()
    }

    private fun initEvent() {
        binding!!.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                type = tab?.text.toString()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding!!.tvBookDetailBookNow.setOnClickListener {
            val hashMap = HashMap<String, String>()
            hashMap[TEACHER_NAME] = binding!!.tvBookDetailTeacherName.text.toString()
            hashMap[STUDENT_NAME] = binding!!.ipBookDetailName.getTextString()
            hashMap[STUDENT_ID] = mContext?.getBySp("userId")!!
            hashMap[REMAIN_TIME] = binding!!.tvBookDetailRemainTime.text.toString()
            hashMap[TIME] = (currentDurationView as TextView).text.toString()
            hashMap[PHONE_NUMBER] = binding!!.ipBookDetailPhone.getTextString()
            hashMap[LOCATION] = binding!!.tvBookDetailLocation.text.toString()
            hashMap[DATE] = binding!!.tvHomeDetailDate.text.toString()
            hashMap[TYPE] = type
            viewModel.bookNow(BOOKING_INFO_CLASS, hashMap, "预约成功!") {
                findNavController().navigate(R.id.action_bookDetailFragment_to_bookInfoFragment)
            }
        }
        binding!!.tvBookDetailLocation.setOnClickListener {
            findNavController().navigate(R.id.action_bookDetailFragment_to_bookInfoFragment)
        }

        group[1].setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        group = requireActivity().findViews()
        group[0].setBackgroundColor(resources.getColor(R.color.statusBarColor, null))
        (group[1] as ImageView).setImageResource(R.drawable.ic_book_back_up)
        (group[2] as TextView).text = "预约信息"
        currentDurationView = binding!!.tvBookDetailFirstDuration
        val mData = data.selectData
        binding!!.tvHomeDetailDate.text =
            "${mData.year}年${mData.month}月${mData.day}日 ${weekArray[mData.week!! - 1]}"
        binding!!.tvBookDetailTeacherName.text = mData.teacher
        selectDuration(binding!!.tvBookDetailFirstDuration, binding!!.tvBookDetailSecondDuration)
    }


    private fun selectDuration(view1: View, view2: View) {
        view1.setOnClickListener {
            currentDurationView = it
            it.background =
                ResourcesCompat.getDrawable(resources, R.drawable.select_radius_bg, null)
            (it as TextView).setTextColor(Color.parseColor("#3170ff"))
            view2.background =
                ResourcesCompat.getDrawable(resources, R.drawable.unselcet_radius_bg, null)
            (view2 as TextView).setTextColor(Color.parseColor("#9eafaf"))
        }

        view2.setOnClickListener {
            currentDurationView = it
            it.background =
                ResourcesCompat.getDrawable(resources, R.drawable.select_radius_bg, null)
            (it as TextView).setTextColor(Color.parseColor("#3170ff"))
            view1.background =
                ResourcesCompat.getDrawable(resources, R.drawable.unselcet_radius_bg, null)
            (view1 as TextView).setTextColor(Color.parseColor("#9eafaf"))
        }
    }

}