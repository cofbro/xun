package com.cofbro.hymvvm.base

/**
 * LoadingState 类是对 DataState 的一个封装，加入了加载信息 --> loadingMsg
 * @param loadingMsg 加载中的信息
 * @param stateFinish 加载中的状态
 */
class LoadingState(val msg: String?, val state: DataState)