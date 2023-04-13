package com.cofbro.xun.book.bookdetail

import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseRepository

class BookDetailRepository : BaseRepository() {
    fun bookNow(
        className: String,
        map: HashMap<String, String>,
        msg: String,
        onSuccess: (LCObject) -> Unit = {}
    ) {
        executeLCRequest {
            leanCloudUtils.saveInLC(className, map, msg, onSuccess)
        }
    }
}