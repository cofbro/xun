package com.cofbro.xun.book.booktime

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.BOOKING_INFO_CLASS
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.CHECKED
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.DATE
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.TEACHER_NAME
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentBookTimeBinding
import com.cofbro.xun.findViews
import com.cofbro.xun.model.params.SelectData
import com.cofbro.xun.splitDateStr
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.hjq.toast.ToastUtils

class BookTimeFragment : BaseFragment<BookTimeViewModel, FragmentBookTimeBinding>() {
    private lateinit var scheduleWrapperList: ArrayList<ScheduleWrapper>
    private lateinit var group: List<View>
    private var mSelectData: SelectData? = null
    private val teacher by navArgs<BookTimeFragmentArgs>()
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        initData()
        initView()
        initEvent()
    }

    private fun initData() {
        loadTeacherSchedule()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        group = requireActivity().findViews()
        setInterceptOnCalendar()
        group[0].setBackgroundColor(Color.WHITE)
        (group[1] as ImageView).setImageResource(R.drawable.ic_book_back_up)
        (group[2] as TextView).text = "${teacher.name}的日程"
    }

    private fun initEvent() {
        // 跳转 -> 进一步预约
        binding!!.tvHomeNextStep.setOnClickListener {
            val date = mSelectData
            if (date == null) {
                ToastUtils.show("请选择预约时间")
            } else {
                val navDirection =
                    BookTimeFragmentDirections.actionBookTimeFragmentToBookDetailFragment(date)
                findNavController().navigate(navDirection)
            }
        }

        // 返回上一级
        group[1].setOnClickListener {
            findNavController().navigateUp()
        }

        // 获取当前选中的日期
        binding!!.calendarView.setOnCalendarSelectListener(object :
            CalendarView.OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar?) {}

            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                mSelectData =
                    SelectData(
                        calendar?.year,
                        calendar?.month,
                        calendar?.day,
                        calendar?.week,
                        teacher.name
                    )
            }
        })
    }

    // 显示日程的空闲，绿色表示已排期
    private fun showScheduleView() {
        val map = HashMap<String, Calendar>()
        val calendar = Calendar()
        scheduleWrapperList.forEach {
            calendar.apply {
                year = it.year
                month = it.month
                day = it.day
            }
            map[calendar.toString()] = calendar
        }
        binding!!.calendarView.setSchemeDate(map)
        binding!!.calendarView.setOnMonthChangeListener { year, month ->
            val string = year.toString() + "年" + month.toString() + "月"
            binding!!.tvBookTime.text = string
        }
    }

    // 拦截日历中的某些日子，点击对应的日期不做显示和处理
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
                ) || ifTeacherScheduleIntercept(calendar)
            }

            override fun onCalendarInterceptClick(calendar: Calendar?, isClick: Boolean) {
                if (calendar!!.isWeekend) {
                    ToastUtils.show("哥们儿周末和同学出去玩，啥烦心事儿都能解决！")
                } else if (ifTeacherScheduleIntercept(calendar)) {
                    ToastUtils.show("已经有小伙伴选择了${calendar.day}号了")
                } else ToastUtils.show("只能从最近14天中选择哦~")

            }
        })
    }

    // 判断该日子是否是在最近14天内
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

    // 获得当前的额日子
    private fun getCurrentMonthLastDay(): Int {
        val a = java.util.Calendar.getInstance()
        a.set(java.util.Calendar.DATE, 1)
        a.roll(java.util.Calendar.DATE, -1)
        return a.get(java.util.Calendar.DATE)
    }

    // 判断老师的日程是否有空闲，否则做拦截
    private fun ifTeacherScheduleIntercept(calendar: Calendar): Boolean {
        scheduleWrapperList.forEach {
            if (calendar.year == it.year && calendar.month == it.month && calendar.day == it.day) {
                return true
            }
        }
        return false
    }

    // 加载当前老师的日程安排情况，已排期在日历上用绿点表示
    private fun loadTeacherSchedule() {
        val map = HashMap<String, String>()
        map[TEACHER_NAME] = teacher.name
        map[CHECKED] = "false"
        viewModel.loadTeacherSchedule(BOOKING_INFO_CLASS, map, "加载成功") {
            scheduleWrapperList = ArrayList()
            it.forEach { lc ->
                val date = splitDateStr(lc.get(DATE).toString())
                scheduleWrapperList.add(
                    ScheduleWrapper(
                        date[0].toInt(),
                        date[3].toInt(),
                        date[2].toInt()
                    )
                )
            }
            showScheduleView()
        }
    }

    // 老师日程类
    class ScheduleWrapper(val year: Int, val month: Int, val day: Int)
}