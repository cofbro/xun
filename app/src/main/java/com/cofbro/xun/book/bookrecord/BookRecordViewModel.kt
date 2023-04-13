package com.cofbro.xun.book.bookrecord

import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseViewModel

class BookRecordViewModel : BaseViewModel<BookRecordRepository>() {
    fun loadBookingRecord(
        className: String,
        equalMap: HashMap<String, String>,
        msg: String,
        onSuccess: (List<LCObject>) -> Unit = {}
    ) {
        repository.loadBookingRecord(className, equalMap, msg, onSuccess)
    }
}