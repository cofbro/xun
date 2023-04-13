package com.cofbro.xun.book.bookinfo

import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseViewModel

class BookInfoViewModel : BaseViewModel<BookInfoRepository>() {
    fun searchTicketInfo(
        className: String,
        equalMap: HashMap<String, String>,
        msg: String,
        onSuccess: (List<LCObject>) -> Unit = {}
    ) {
        repository.searchTicketInfo(className, equalMap, msg, onSuccess)
    }
}