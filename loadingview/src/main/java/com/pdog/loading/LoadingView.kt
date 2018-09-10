package com.pdog.loading

import android.app.Activity
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.pdog.R

interface LoadingView {

    /**
     * 开始加载状态
     */
    fun startLoading() {
        getMultiStateView().viewState = MultiStateView.VIEW_STATE_LOADING
    }

    /**
     * 关闭加载状态
     */
    fun loadingFinish() {
        getMultiStateView().viewState = MultiStateView.VIEW_STATE_CONTENT
    }

    /**
     * 请求成功，响应数据为空
     */
    fun loadingEmpty() {
        getMultiStateView().viewState = MultiStateView.VIEW_STATE_EMPTY
    }

    /**
     * 加载失败，显示错误界面
     */
    fun loadingError(exception: RuntimeException) {
        getMultiStateView().viewState = MultiStateView.VIEW_STATE_ERROR
    }

    fun getMultiStateView(): MultiStateView {
        if (getRootView().findViewById<View>(R.id.multiStateView) == null) {
            setupMultiStateView()
        }
        return getRootView().findViewById(R.id.multiStateView)
    }

    /**
     * 找到对应的 contentView，然后创建[MultiStateView]并且加入到content的parent中，
     */
    fun setupMultiStateView() {
        val contentView = getContentView()

        val layoutParams = contentView.layoutParams
        val parent = contentView.parent as ViewGroup
        val contentIndex = parent.indexOfChild(contentView)

        parent.run {
            removeViewAt(contentIndex)

            val stateView = MultiStateView(parent.context).apply {
                id = R.id.multiStateView
                setViewForState(contentView, MultiStateView.VIEW_STATE_CONTENT)
                setViewForState(getErrorStateView(), MultiStateView.VIEW_STATE_ERROR)
                setViewForState(getEmptyStateView(), MultiStateView.VIEW_STATE_EMPTY)
                setViewForState(getLoadingStateView(), MultiStateView.VIEW_STATE_LOADING)
//                setAnimateLayoutChanges(true)
                viewState = MultiStateView.VIEW_STATE_LOADING
            }

            addView(stateView, contentIndex, layoutParams)
        }
    }


    /**
     * 0. 如果子类有需要，直接重写该方法
     * 1. 先尝试查找布局文件中添加了[CONTENT_DESCRIPTION] 的指定View
     * 2. 如果没有，那么使用默认规则的RootView的第一个Child
     */
    fun getContentView(): View {
        val root = getRootView()

        return try {
            findViewByText(root, root.context.getString(R.string.content))
        } catch (exception: NoSuchElementException) {
            root.getChildAt(0)
        }
    }


    fun getErrorStateView() = R.layout.view_state_error
    fun getEmptyStateView() = R.layout.view_state_empty
    fun getLoadingStateView() = R.layout.view_state_loading


    private fun getRootView() = when (this) {
        is Activity -> this.findViewById(android.R.id.content)
        is Fragment -> this.view as ViewGroup
        else -> throw IllegalArgumentException("not activity or fragment")
    }

    private fun findViewByText(viewGroup: ViewGroup, text: CharSequence): View {
        val views = arrayListOf<View>()
        viewGroup.findViewsWithText(views, text, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
        return views.first()
    }
}

