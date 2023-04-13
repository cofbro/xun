package com.cofbro.xun.book.booktime

import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseViewModel
import com.cofbro.xun.book.BookRepository

class BookTimeViewModel : BaseViewModel<BookTimeRepository>() {
    // 加载老师的日程空闲情况
    fun loadTeacherSchedule(
        className: String,
        equalMap: HashMap<String, String>,
        msg: String?,
        onSuccess: (List<LCObject>) -> Unit = {}
    ) {
        repository.loadTeacherSchedule(className, equalMap, msg, onSuccess)
    }
}