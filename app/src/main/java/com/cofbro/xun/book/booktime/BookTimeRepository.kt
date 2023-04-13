package com.cofbro.xun.book.booktime

import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseRepository

class BookTimeRepository : BaseRepository() {
    fun loadTeacherSchedule(
        className: String,
        equalMap: HashMap<String, String>,
        msg: String?,
        onSuccess: (List<LCObject>) -> Unit = {}
    ) {
        executeLCRequest {
            leanCloudUtils.searchInLC(className, equalMap, msg, onSuccess)
        }
    }
}