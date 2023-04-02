package com.cofbro.hymvvm.lean

import androidx.lifecycle.MutableLiveData
import cn.leancloud.LCObject
import cn.leancloud.LCUser
import cn.leancloud.types.LCNull
import com.cofbro.hymvvm.base.DataState
import com.hjq.toast.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * LeanCloud 在结构中类似于Retrofit API，也可以就把他当做Retrofit API来使用
 *
 * LeanCloud 工具类，包含注册，登录，储存，删除，查询等通用Api
 *
 * 内部同样封装好了请求状态，使用者无需关心
 *
 * 注意：如果需要用此工具包，需要设置全局参数 isUsedLeanCloud
 *
 * 如果设置了全局参数，但部分页面并未使用 LeanCloudUtils，
 *
 * 可以重写 Activity/Fragment 中的 isUseLeanCloud()
 *
 * @see BaseFragment.isUseLeanCloud()
 * @see BaseActivity.isUseLeanCloud()
 */
class LeanCloudUtils {

    val leanCloudLiveData by lazy {
        MutableLiveData<DataState>()
    }

    companion object {
        private var isUseLeanCloud = false
        fun init(isUseLeanCloud: Boolean) {
            this.isUseLeanCloud = isUseLeanCloud
        }

        fun isUsed(): Boolean {
            return isUseLeanCloud
        }
    }


    /**
     * 储存
     * @param result 待储存对象中的参数，如：name = "cofbro"
     * @param onSuccess 成功回调
     */
    fun <T : Any> LCObject.saveInLC(result: CommResult<T>, onSuccess: (LCObject) -> Unit = {}) {
        checkIsUsed()
        leanCloudLiveData.postValue(DataState.STATE_LOADING)
        val todo = iterateAllProperties(this, result)
        todo.saveInBackground().subscribe(ObserverImpl<LCObject> {
            onSuccess(it)
        })
    }

    /**
     * 注册，需要验证邮箱，否则注册不成功
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱地址
     * @param isTeacher 是否是老师
     * @param onSuccess 成功回调
     */
    fun register(
        username: String,
        password: String,
        email: String,
        isTeacher: Boolean,
        onSuccess: (LCUser) -> Unit = {}
    ) {
        checkIsUsed()
        leanCloudLiveData.postValue(DataState.STATE_LOADING)
        val user = LCUser()
        user.username = username
        user.password = password
        user.email = email
        user.put("isTeacher", isTeacher)
        user.signUpInBackground().subscribe(ObserverImpl<LCUser> {
            onSuccess(it)
        })
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @param onSuccess 成功回到
     */
    fun login(
        username: String,
        password: String,
        onSuccess: (LCUser) -> Unit = {}
    ) {
        checkIsUsed()
        leanCloudLiveData.postValue(DataState.STATE_LOADING)
        LCUser.logIn(username, password).subscribe(ObserverImpl<LCUser> {
            onSuccess(it)
        })

    }

    /**
     * 判断是否是登录状态
     */
    fun isLogin(): Boolean {
        return LCUser.getCurrentUser() != null
    }

    /**
     * 登出，云端登出
     */
    fun logout() {
        LCUser.logOut()
    }

    /**
     * 重置密码
     * @param email 邮箱地址
     * @param onSuccess 成功回调
     */
    fun resetPassword(email: String, onSuccess: () -> Unit) {
        checkIsUsed()
        leanCloudLiveData.postValue(DataState.STATE_LOADING)
        LCUser.requestPasswordResetInBackground(email).subscribe(ObserverImpl<LCNull> {
            onSuccess()
        })
    }

    /**
     * 用于遍历对象中的所有属性
     * @param obj 待储存对象 --> LCObject
     * @param result 待加入的参数对象
     */
    private fun <T : Any> iterateAllProperties(obj: LCObject, result: CommResult<T>): LCObject {
        try {
            // 通获取对象类中的所有属性
            val fields = result.data?.javaClass!!.declaredFields
            fields.forEach {
                // 设置允许通过反射访问私有变量
                it.isAccessible = true
                // 获取字段的值
                val value = it.get(result.data)?.toString()
                // 获取字段属性名称
                val name = it.name
                // 添加所有键值对
                obj.put(name, value)
            }
        } catch (e: Exception) {
        }
        return obj
    }

    /**
     * 是否使用 LeanCloud
     */
    private fun checkIsUsed() {
        if (!isUsed()) return
    }

    /**
     * reactivex.Observer 实现类，防止每次 new 一个匿名内部类
     * @param onSuccess 成功回调
     */
    inner class ObserverImpl<T : Any>(val onSuccess: (T) -> Unit = {}) : Observer<T> {
        override fun onSubscribe(d: Disposable) {}

        override fun onNext(t: T) {
            leanCloudLiveData.postValue(DataState.STATE_SUCCESS)
            onSuccess(t)
        }

        override fun onError(e: Throwable) {
            ToastUtils.show(e)
        }

        override fun onComplete() {}
    }
}