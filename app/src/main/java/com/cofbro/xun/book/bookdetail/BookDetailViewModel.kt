package com.cofbro.xun.book.bookdetail

import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseViewModel

class BookDetailViewModel : BaseViewModel<BookDetailRepository>() {
    fun bookNow(className: String,
                map: HashMap<String, String>,
                msg: String,
                onSuccess: (LCObject) -> Unit = {}) {
        repository.bookNow(className, map, msg, onSuccess)
    }
}