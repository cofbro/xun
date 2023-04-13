package com.cofbro.xun.community


import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseRepository
import com.luck.picture.lib.entity.LocalMedia
import kotlin.collections.ArrayList

class CommunityRepository : BaseRepository() {
    // 星球上传图片
    fun uploadPlanetContent(
        medias: List<LocalMedia>,
        msg: String?,
        onComplete: (List<String>) -> Unit = {}
    ) {
        val files = ArrayList<String>()
        executeLCRequest {
            medias.forEach {
                leanCloudUtils.saveFileInLC(it.fileName, it.availablePath, msg) { file ->
                    files.add(file.url)
                    if (files.size == medias.size) {
                        onComplete(files)
                    }
                }
            }
        }
    }

    // 图文关联
    fun attachOn(
        lcObject: LCObject,
        msg: String?,
        onSuccess: (LCObject) -> Unit = {}
    ) {
        executeLCRequest {
            leanCloudUtils.saveInLC(lcObject, msg, onSuccess)
        }
    }

    // 查询星球图文
    fun loadPlanetContent(
        className: String,
        equalMap: HashMap<String, String>?,
        msg: String?,
        onSuccess: (List<LCObject>) -> Unit = {}
    ) {
        executeLCRequest {
            leanCloudUtils.searchInLC(className, equalMap, msg, onSuccess)
        }
    }

}