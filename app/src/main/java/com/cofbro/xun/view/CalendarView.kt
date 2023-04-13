package com.cofbro.xun.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.cofbro.xun.R

import com.cofbro.xun.view.CalendarView.Config.DEFAULT_PADDING
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.MonthView


/**
 * 定制高仿魅族日历界面，按你的想象力绘制出各种各样的界面
 *
 */
class CalendarView(context: Context?) : MonthView(context) {
    object Config {
        const val DEFAULT_PADDING = 8f
    }

    private val normalTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        isDither = true
        strokeWidth = 1.3f
        textSize = 38f
    }
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = resources.getColor(R.color.appColor, null)
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        isDither = true
        strokeWidth = 1f
    }

    private val schemaPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#34c759")
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        isDither = true
        strokeWidth = 1f
    }

    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
     */
    override fun onDrawSelected(
        canvas: Canvas?,
        calendar: Calendar,
        x: Int,
        y: Int,
        hasScheme: Boolean
    ): Boolean {
        //这里绘制选中的日子样式，看需求需不需要继续调用onDrawScheme
        val l = x.toFloat()
        val t = y.toFloat()
        val r = l + mItemWidth
        val b = t + mItemHeight
        canvas?.drawRoundRect(
            l + DEFAULT_PADDING,
            t + DEFAULT_PADDING,
            r - DEFAULT_PADDING,
            b - DEFAULT_PADDING,
            35f,
            35f,
            backgroundPaint
        )
        return true
    }

    /**
     * 绘制标记的事件日子
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    override fun onDrawScheme(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int) {
        //这里绘制标记的日期样式，想怎么操作就怎么操作
        canvas?.drawCircle(x + mItemWidth / 2f, y + mItemHeight - 5 * DEFAULT_PADDING, 3f, schemaPaint)
    }

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    override fun onDrawText(
        canvas: Canvas?,
        calendar: Calendar,
        x: Int,
        y: Int,
        hasScheme: Boolean,
        isSelected: Boolean
    ) {
        val string = calendar.day.toString()
        val bounds = Rect()
        normalTextPaint.getTextBounds(string, 0, string.length, bounds)
        val dx = x + (mItemWidth - DEFAULT_PADDING * 2 - bounds.width()) / 2f + 6f
        val dy =
            y + (mItemHeight - DEFAULT_PADDING * 2 - bounds.height()) / 2f - normalTextPaint.ascent()
        if (isSelected) {
            normalTextPaint.color = Color.WHITE
            canvas?.drawText(string, 0, string.length, dx, dy, normalTextPaint)
        } else {
            normalTextPaint.color = Color.parseColor("#9eafaf")
            canvas?.drawText(string, 0, string.length, dx, dy, normalTextPaint)
        }

    }
}