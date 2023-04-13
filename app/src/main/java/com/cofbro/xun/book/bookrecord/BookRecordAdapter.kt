package com.cofbro.xun.book.bookrecord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.DATE
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.LOCATION
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.TEACHER_NAME
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.TIME
import com.cofbro.xun.databinding.BookRecordItemLayoutBinding
import com.cofbro.xun.splitDateStr

class BookRecordAdapter(var userInfoList: List<LCObject>?) : RecyclerView.Adapter<BookRecordAdapter.BookRecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookRecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookRecordItemLayoutBinding.inflate(inflater, parent, false)
        return BookRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookRecordViewHolder, position: Int) {
        if (userInfoList == null) return
        holder.bind(userInfoList!![position])
    }

    override fun getItemCount(): Int {
        if (userInfoList == null) return 0
        return userInfoList!!.size
    }

    class BookRecordViewHolder(val binding: BookRecordItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userInfo: LCObject) {
            val teacherName = userInfo.get(TEACHER_NAME).toString()
            val location = userInfo.get(LOCATION).toString()
            val time = userInfo.get(TIME).toString()
            val id = userInfo.objectId.toString()
            val date = splitDateStr(userInfo.get(DATE).toString())
            binding.tvBookRecordTeacherName.text = teacherName
            binding.tvBookRecordLocation.text = location
            binding.tvBookRecordTime.text = time
            binding.tvBookRecordBookId.text = id
            binding.tvBookReocrdYear.text = date[0]
            binding.tvBookReocrdMonth.text = date[1]
            binding.tvBookReocrdDay.text = date[2]
        }

    }
}