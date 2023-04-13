package com.cofbro.xun.community

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.CONTENT_PLANET
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.IMG_URL_PLANET
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.USER_NAME_PLANET
import com.cofbro.xun.databinding.CommunityContentItemLayoutBinding

/**
 * 图文中展示的图片列表适配器
 */
class CommunityContentAdapter(var contents: List<LCObject>) :
    RecyclerView.Adapter<CommunityContentAdapter.CommunityContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityContentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommunityContentItemLayoutBinding.inflate(inflater, parent, false)
        return CommunityContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityContentViewHolder, position: Int) {
        holder.bind(contents[position])
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    @Suppress("UNCHECKED_CAST")
    inner class CommunityContentViewHolder(val binding: CommunityContentItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LCObject) {
            binding.tvCommunityName.text = data.get(USER_NAME_PLANET)?.toString()
            binding.tvCommunityContent.text = data.get(CONTENT_PLANET)?.toString()
            binding.tvCommunityTime.text = data.createdAt.toString()
            val urls = (data.get(IMG_URL_PLANET) as List<*>)
            binding.rvCommunityPic.apply {
                adapter = CommunityIMGAdapter(urls as List<String>)
                layoutManager = GridLayoutManager(binding.root.context, 3, RecyclerView.VERTICAL, false)
                addItemDecoration(object : RecyclerView.ItemDecoration(){
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.set(5, 15, 5, 15)
                    }
                })
            }
        }
    }


}


