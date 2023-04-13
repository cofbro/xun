package com.cofbro.xun.community

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseFragment
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.CONTENT_PLANET
import com.cofbro.xun.GlideEngine
import com.cofbro.xun.R
import com.cofbro.xun.databinding.FragmentCommunityBinding
import com.cofbro.xun.findViews
import com.hjq.toast.ToastUtils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.SandboxTransformUtils


class CommunityFragment : BaseFragment<CommunityViewModel, FragmentCommunityBinding>() {
    private lateinit var selectImageAdapter: CommunityEditIMGAdapter
    private lateinit var contentAdapter: CommunityContentAdapter
    private var albumList: MutableList<LocalMedia> = mutableListOf()
    private var contentList: MutableList<LCObject> = mutableListOf()
    private lateinit var group: List<View>
    override fun onAllViewCreated(savedInstanceState: Bundle?) {
        initData()
        initView()
        initEvent()
    }

    private fun initData() {
        viewModel.loadPlanetContent(null, null) {
            contentList.addAll(it)
            contentAdapter.notifyDataSetChanged()
        }
    }

    private fun initView() {
        // 导航栏
        group = requireActivity().findViews()
        group[0].setBackgroundColor(resources.getColor(R.color.statusBarColor, null))
        (group[1] as ImageView).setImageResource(R.drawable.ic_more)
        (group[2] as TextView).text = "星球社区"
        // 输入框相册rv
        binding!!.rvCommunityInput.apply {
            selectImageAdapter = CommunityEditIMGAdapter(albumList, 9)
            adapter = selectImageAdapter
            layoutManager = GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.set(10, 20, 10, 20)
                }
            })
        }
        // 星球内容rv
        binding!!.rvCommunityContent.apply {
            contentAdapter = CommunityContentAdapter(contentList)
            adapter = contentAdapter
            layoutManager = LinearLayoutManager(mContext,  RecyclerView.VERTICAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.set(0, 20, 0, 20)
                }
            })
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    Log.d("chy", "onScrolled: dy :$dy")
//                    if (dy > 0) {
//                        binding!!.csCommunityInput.visibility = View.GONE
//                    } else {
//                        binding!!.csCommunityInput.visibility = View.VISIBLE
//                    }
                }
            })
        }
    }

    private fun initEvent() {
        // 导航栏
        group[1].setOnClickListener {
            (group[4] as DrawerLayout).openDrawer(GravityCompat.START)
        }

        // 进入相册，返回时携带图片地址
        selectImageAdapter.setOnMyClickListener(object : CommunityEditIMGAdapter.OnItemClickListener {
            override fun onItemAddClick(position: Int) {
                PictureSelector
                    .create(requireActivity())
                    .openGallery(SelectMimeType.ofImage())
                    .setSandboxFileEngine { context, srcPath, mineType, call ->
                        if (call != null) {
                            val sandboxPath =
                                SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType)
                            call.onCallback(srcPath, sandboxPath);
                        }
                    }
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: ArrayList<LocalMedia>?) {
                            albumList.addAll(result!!)
                            selectImageAdapter.notifyDataSetChanged()
                        }

                        override fun onCancel() {}
                    })
            }

            override fun onItemDelClick(position: Int) {
                albumList.removeAt(position)
                selectImageAdapter.notifyDataSetChanged()
            }

            override fun onItemPicClick(position: Int) {

            }
        })

        binding!!.root.setOnClickListener {
            it.scaleX = 0f
            it.scaleY = 0f
            it.animate()
                .scaleY(1f)
                .scaleX(1f)
                .setDuration(300)
                .setInterpolator(DecelerateInterpolator())
                .start()

        }

        // 星球图文发布
        group[3].setOnClickListener {
            val map = HashMap<String, String>()
            map[CONTENT_PLANET] = binding!!.tvCommunityInput.text.toString()
            ToastUtils.show("发布成功！")
            albumList.clear()
            selectImageAdapter.notifyDataSetChanged()
            viewModel.uploadPlanetContent(albumList, null) {
                binding!!.tvCommunityInput.text.clear()
            }
        }
    }
}