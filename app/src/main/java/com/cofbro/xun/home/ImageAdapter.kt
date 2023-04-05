package com.cofbro.xun.home

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter


class ImageAdapter(private val data: List<Int>?) :
    BannerAdapter<Int, ImageAdapter.BannerViewHolder>(data) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        // 必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(holder: BannerViewHolder, data: Int, position: Int, size: Int) {
        holder.imageView.setImageResource(data)
    }

    inner class BannerViewHolder(var imageView: ImageView) :
        RecyclerView.ViewHolder(imageView)
}