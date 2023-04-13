package com.cofbro.xun.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cofbro.xun.databinding.CommunityContentImgLayoutBinding

class CommunityIMGAdapter(private val urls: List<String>) : RecyclerView.Adapter<CommunityIMGAdapter.CommunityIMGViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityIMGViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommunityContentImgLayoutBinding.inflate(inflater, parent ,false)
        return CommunityIMGViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityIMGViewHolder, position: Int) {
        holder.bind(urls[position])
    }

    override fun getItemCount(): Int {
        return urls.size
    }

    class CommunityIMGViewHolder(val binding: CommunityContentImgLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            Glide.with(binding.root.context)
                .load(url)
                .into(binding.img)
        }

    }
}