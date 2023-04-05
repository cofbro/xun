package com.cofbro.xun.book.bookdetail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.xun.R
import com.cofbro.xun.book.BookViewModel
import com.cofbro.xun.databinding.FragmentBookDetailBinding

class BookDetailFragment : BaseFragment<BookViewModel, FragmentBookDetailBinding>() {
    private lateinit var currentDurationView: View
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        selectDuration(binding!!.tvBookDetailFirstDuration, binding!!.tvBookDetailSecondDuration)
    }


    private fun selectDuration(view1: View, view2: View ) {
        view1.setOnClickListener {
            currentDurationView = it
            it.background = ResourcesCompat.getDrawable(resources, R.drawable.select_radius_bg, null)
            (it as TextView).setTextColor(Color.parseColor("#3170ff"))
            view2.background = ResourcesCompat.getDrawable(resources, R.drawable.unselcet_radius_bg, null)
            (view2 as TextView).setTextColor(Color.parseColor("#9eafaf"))
        }

        view2.setOnClickListener {
            currentDurationView = it
            it.background = ResourcesCompat.getDrawable(resources, R.drawable.select_radius_bg, null)
            (it as TextView).setTextColor(Color.parseColor("#3170ff"))
            view1.background = ResourcesCompat.getDrawable(resources, R.drawable.unselcet_radius_bg, null)
            (view1 as TextView).setTextColor(Color.parseColor("#9eafaf"))
        }
    }




}