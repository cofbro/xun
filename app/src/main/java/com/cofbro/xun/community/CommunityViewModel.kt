package com.cofbro.xun.community

import cn.leancloud.LCObject
import com.cofbro.hymvvm.base.BaseViewModel
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.IMG_URL_PLANET
import com.cofbro.hymvvm.lean.LeanCloudUtils.LCConstant.PLANET_CONTENT_CLASS
import com.luck.picture.lib.entity.LocalMedia

class CommunityViewModel : BaseViewModel<CommunityRepository>() {
    // 星球上传图文
    fun uploadPlanetContent(
        medias: List<LocalMedia>,
        msg: String?,
        onSuccess: () -> Unit = {}
        ) {
        repository.uploadPlanetContent(medias, null) {
            val todo = LCObject(PLANET_CONTENT_CLASS)
            todo.put(IMG_URL_PLANET, it)
            repository.attachOn(todo, msg) {
                onSuccess()
            }
        }
    }

    // 查询星球图文
    fun loadPlanetContent(
        equalMap: HashMap<String, String>?,
        msg: String?,
        onSuccess: (List<LCObject>) -> Unit = {}
    ) {
        repository.loadPlanetContent(PLANET_CONTENT_CLASS, equalMap, msg, onSuccess)
    }
}