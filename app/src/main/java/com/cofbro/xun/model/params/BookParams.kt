package com.cofbro.xun.model.params

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class BookParams(
    val id: String,
    val teacher: String,
    val date: String,
    val location: String,
    val remainTime: String,
    val duration: String,
    val fullName: String,
    val phoneNumber: String,
    val type: String
)

@Parcelize
class SelectData(
    val year: Int?,
    val month: Int?,
    val day: Int?,
    val week: Int?,
    val teacher: String
) :
    Parcelable