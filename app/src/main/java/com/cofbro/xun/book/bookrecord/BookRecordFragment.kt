package com.cofbro.xun.book.bookrecord

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.hymvvm.base.SP_USER_ID
import com.cofbro.hymvvm.base.getBySp
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.BOOKING_INFO_CLASS
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.STUDENT_ID
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentBookRecordBinding
import com.cofbro.xun.findViews

class BookRecordFragment : BaseFragment<BookRecordViewModel, FragmentBookRecordBinding>() {
    private lateinit var group: List<View>
    private var bookRecordInfoList: List<LCObject>? = null
    private var bookRecordInfoAdapter: BookRecordAdapter? = null
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        initData()
        initView()
        initEvent()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData() {
        val map = HashMap<String, String>()
        map[STUDENT_ID] = mContext?.getBySp(SP_USER_ID)!!
        viewModel.loadBookingRecord(BOOKING_INFO_CLASS, map, "加载成功") {
            if (bookRecordInfoAdapter != null) {
                bookRecordInfoList = it
                bookRecordInfoAdapter!!.userInfoList = it
                bookRecordInfoAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun initView() {
        // 状态栏
        group = requireActivity().findViews()
        group[0].setBackgroundColor(resources.getColor(R.color.statusBarColor, null))
        (group[1] as ImageView).setImageResource(R.drawable.ic_book_back_up)
        (group[2] as TextView).text = "预约信息"
        // rv
        bookRecordInfoAdapter = BookRecordAdapter(bookRecordInfoList)
        binding!!.rvBookRecord.apply {
            adapter = bookRecordInfoAdapter
            layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        }

    }

    private fun initEvent() {
        group[1].setOnClickListener {
            findNavController().navigateUp()
        }
    }
}