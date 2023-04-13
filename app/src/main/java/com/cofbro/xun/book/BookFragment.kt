package com.cofbro.xun.book

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentBookBinding
import com.cofbro.xun.findViews

class BookFragment : BaseFragment<BookViewModel, FragmentBookBinding>() {
    private lateinit var group: List<View>
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        initView()
        initEvent()
    }

    private fun initView() {
        group = requireActivity().findViews()
        group[0].setBackgroundColor(resources.getColor(R.color.statusBarColor, null))
        (group[1] as ImageView).setImageResource(R.drawable.ic_more)
        (group[2] as TextView).text = "选择老师"
    }

    private fun initEvent() {
        binding!!.csBookFragmentTeacher.setOnClickListener {
            val action =
                BookFragmentDirections.actionBookFragmentToBookTimeFragment(binding!!.tvBookTeacherName.text.toString())
            findNavController().navigate(action)
        }
        group[1].setOnClickListener {
            (group[4] as DrawerLayout).openDrawer(GravityCompat.START)
        }
    }


}