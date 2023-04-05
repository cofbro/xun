package com.cofbro.xun.book

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentBookBinding
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.hjq.toast.ToastUtils
import java.util.Calendar.DATE

class BookFragment : BaseFragment<BookViewModel, FragmentBookBinding>() {
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        setInterceptOnCalendar()
        bindViewWithTime()
        binding!!.tvHomeNextStep.setOnClickListener {
            findNavController().navigate(R.id.action_bookFragment_to_bookDetailFragment)
        }


    }

    private fun bindViewWithTime() {
        val map = HashMap<String, Calendar>()
        val calendar = Calendar()
        calendar.apply {
            year = 2023
            month = 4
            day = 10
        }
        map[calendar.toString()] = calendar
        binding!!.calendarView.setSchemeDate(map)
        binding!!.calendarView.setOnMonthChangeListener { year, month ->
            val string = year.toString() + "年" + month.toString() + "月"
            binding!!.tvBookTime.text = string
        }
    }

    private fun setInterceptOnCalendar() {
        binding!!.calendarView.setOnCalendarInterceptListener(object :
            CalendarView.OnCalendarInterceptListener {
            override fun onCalendarIntercept(calendar: Calendar): Boolean {
                val currentDay = binding!!.calendarView.curDay
                val currentMonth = binding!!.calendarView.curMonth
                return calendar.isWeekend || judgmentIfValid(
                    currentDay,
                    currentMonth,
                    calendar.day,
                    calendar.month
                )
            }

            override fun onCalendarInterceptClick(calendar: Calendar?, isClick: Boolean) {
                if (calendar!!.isWeekend) {
                    ToastUtils.show("哥们儿周末和同学出去玩，啥烦心事儿都能解决！")
                } else ToastUtils.show("只能从最近14天中选择哦~")

            }
        })
    }

    private fun judgmentIfValid(
        currentDay: Int,
        currentMonth: Int,
        selectDay: Int,
        selectMonth: Int
    ): Boolean {
        if (currentMonth == selectMonth) {
            if (currentDay + 14 >= selectDay) return false
        } else {
            if (currentMonth > selectMonth) return true
            if (currentDay + 14 <= getCurrentMonthLastDay()) {
                return true
            } else {
                if ((currentDay + 14) % getCurrentMonthLastDay() >= selectDay) return false
            }
        }
        return true
    }

    private fun getCurrentMonthLastDay(): Int {
        val a = java.util.Calendar.getInstance()
        a.set(DATE, 1)
        a.roll(DATE, -1)
        return a.get(DATE)
    }
}