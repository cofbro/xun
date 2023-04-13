package com.cofbro.xun.book.bookinfo

import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseRepository

class BookInfoRepository : BaseRepository() {
    fun searchTicketInfo(
        className: String,
        equalMap: HashMap<String, String>,
        msg: String,
        onSuccess: (List<LCObject>) -> Unit = {}
    ) {
        executeLCRequest { leanCloudUtils.searchInLC(className, equalMap, msg, onSuccess) }
    }
}