package com.cofbro.xun.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cofbro.xun.databinding.AlbumAddIntoLayoutBinding
import com.cofbro.xun.databinding.AlbumItemLayoutBinding
import com.luck.picture.lib.entity.LocalMedia

class CommunityEditIMGAdapter(var data: MutableList<LocalMedia>, private val maxNum: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ADD_ITEM = 1
    private val PIC_ITEM = 2
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ADD_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = AlbumAddIntoLayoutBinding.inflate(layoutInflater, parent, false)
                AddViewHolder(view)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = AlbumItemLayoutBinding.inflate(layoutInflater, parent, false)
                PicViewHolder(view)
            }
        }
    }

    /**
     * 当数量小于最大值时，返回的数量+1，为了给加号布局添加位置
     * 否则就返回正常的数据大小(达到最大值后，不用给加号布局添加位置)
     */
    override fun getItemCount(): Int {
        return if (data.size < maxNum) {
            data.size + 1
        } else {
            data.size
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //加号的布局
        if (holder is AddViewHolder) {
            holder.bind(position)
        }
        //加载图片的布局
        else {
            (holder as PicViewHolder).bind(position)
        }
    }

    /**
     * 当数量达到最大值时，返回图片布局
     * 否则，如果当前位置 + 1 = itemCount，则代表它是最后一个，因为位置是从0计数的，而itemCount是从1计数
     */
    override fun getItemViewType(position: Int): Int {

        return if (data.size == maxNum) {
            PIC_ITEM
        } else {
            if (position + 1 == itemCount) {
                ADD_ITEM
            } else {
                PIC_ITEM
            }
        }

    }

    // 加号布局
    inner class AddViewHolder(val binding: AlbumAddIntoLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.ivAdd.setOnClickListener {
                onItemClickListener?.onItemAddClick(position)
            }
        }
    }


    // 普通布局
    inner class PicViewHolder(val binding: AlbumItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Glide.with(binding.root).load(data[position].path)
                .into(binding.ivAlbum)
            binding.ivAlbum.setOnClickListener {
                onItemClickListener?.onItemPicClick(position)
            }
            binding.ivDelete.setOnClickListener {
                onItemClickListener?.onItemDelClick(position)
            }
        }
    }


    // 设置接口回调来实现点击功能
    fun setOnMyClickListener(onClickListener: OnItemClickListener?) {
        onItemClickListener = onClickListener
    }

    interface OnItemClickListener {
        //点击增加按键
        fun onItemAddClick(position: Int)

        //点击删除按键
        fun onItemDelClick(position: Int)

        //点击图片
        fun onItemPicClick(position: Int)
    }
}
