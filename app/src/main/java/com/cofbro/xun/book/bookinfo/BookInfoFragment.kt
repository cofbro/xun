package com.cofbro.xun.book.bookinfo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.hymvvm.base.SP_USER_ID
import com.cofbro.hymvvm.base.getBySp
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.BOOKING_INFO_CLASS
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.STUDENT_ID
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentBookInfoBinding
import com.cofbro.xun.findViews

class BookInfoFragment : BaseFragment<BookInfoViewModel, FragmentBookInfoBinding>() {
    private lateinit var group: List<View>
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        initView()
        initEvent()
    }

    private fun initView() {
        group = requireActivity().findViews()
        group[0].setBackgroundColor(Color.WHITE)
        (group[1] as ImageView).setImageResource(R.drawable.ic_book_back_up)
        (group[2] as TextView).text = "预约信息"
    }

    private fun initEvent() {
        searchTicketInfo()
        // 确定按钮
        binding!!.tvBookInfoOk.setOnClickListener {
            findNavController().navigate(R.id.action_bookInfoFragment_to_bookFragment)
        }

        // 返回按钮
        group[1].setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun searchTicketInfo() {
        val map = HashMap<String, String>()
        map[STUDENT_ID] = mContext?.getBySp(SP_USER_ID)!!
        viewModel.searchTicketInfo(BOOKING_INFO_CLASS, map, "加载成功") {
            if (it.isNotEmpty()) {
                val ticket = it[0]
                val username = ticket.get("studentName")
                val ticketId = ticket.objectId
                val date = ticket.get("date")
                val location = ticket.get("location")
                val time = ticket.get("time")
                binding!!.tvBookInfoName.text = username.toString()
                binding!!.tvBookInfoTicketId.text = ticketId.toString()
                binding!!.tvBookInfoDate.text = date.toString()
                binding!!.tvBookDetailTime.text = time.toString()
                binding!!.tvBookInfoLocation.text = location.toString()
            }
        }
    }
}