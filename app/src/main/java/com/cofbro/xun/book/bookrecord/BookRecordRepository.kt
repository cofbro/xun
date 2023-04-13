package com.cofbro.xun.book.bookrecord

import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseRepository

class BookRecordRepository : BaseRepository() {
    fun loadBookingRecord(
        className: String,
        equalMap: HashMap<String, String>,
        msg: String,
        onSuccess: (List<LCObject>) -> Unit = {}
    ) {
        executeLCRequest {
            leanCloudUtils.searchInLC(className, equalMap, msg, onSuccess)
        }
    }
}