package com.pdog.loading

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

class MultiStateView(context: Context) : FrameLayout(context) {
    private var mInflater: LayoutInflater? = null
    private var mContentView: View? = null
    private var mLoadingView: View? = null
    private var mErrorView: View? = null
    private var mEmptyView: View? = null
    private var mAnimateViewChanges: Boolean = false
    private var mListener: MultiStateView.StateListener? = null
    private var mViewState: Int = 0

    var viewState: Int
        get() = this.mViewState
        set(state) {
            if (state != this.mViewState) {
                val previous = this.mViewState
                this.mViewState = state
                this.setView(previous)
                if (this.mListener != null) {
                    this.mListener!!.onStateChanged(this.mViewState)
                }
            }
        }


    init {
        this.mAnimateViewChanges = false
        this.mViewState = -1

        this.mInflater = LayoutInflater.from(this.context)
        this.mViewState = 0
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (this.mContentView == null) {
            throw IllegalArgumentException("Content view is not defined")
        } else {
            this.setView(-1)
        }
    }

    private fun validContentView(child: View?) {
        if (this.isValidContentView(child)) {
            this.mContentView = child
        }
    }

    override fun addView(child: View?) {
        validContentView(child)
        super.addView(child)
    }

    override fun addView(child: View, index: Int) {
        validContentView(child)
        super.addView(child, index)
    }

    override fun addView(child: View, width: Int, height: Int) {
        validContentView(child)
        super.addView(child, width, height)
    }

    fun getViewByState(state: Int): View? {
        return when (state) {
            0 -> this.mContentView
            1 -> this.mErrorView
            2 -> this.mEmptyView
            3 -> this.mLoadingView
            else -> null
        }
    }

    private fun setView(previousState: Int) {
        val stateArray = intArrayOf(
                (MultiStateView.VIEW_STATE_CONTENT),
                (MultiStateView.VIEW_STATE_LOADING),
                (MultiStateView.VIEW_STATE_ERROR),
                (MultiStateView.VIEW_STATE_EMPTY))

        stateArray.forEach {
            val view = getViewByState(it)
            if (it != this.mViewState) {
                view?.visibility = View.GONE
            } else {
                if (this.mAnimateViewChanges) {
                    this.animateLayoutChange(this.getViewByState(previousState))
                } else {
                    view!!.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun isValidContentView(view: View?): Boolean {
        return if (this.mContentView != null && this.mContentView !== view)
            false
        else
            view !== this.mLoadingView
                    && view !== this.mErrorView
                    && view !== this.mEmptyView
    }

    fun setViewForState(view: View, state: Int, switchToState: Boolean) {
        val stateView = getViewByState(state)
        this.removeView(stateView)

        when (state) {
            MultiStateView.VIEW_STATE_CONTENT -> this.mContentView = view
            MultiStateView.VIEW_STATE_ERROR -> this.mErrorView = view
            MultiStateView.VIEW_STATE_EMPTY -> this.mEmptyView = view
            MultiStateView.VIEW_STATE_LOADING -> this.mLoadingView = view
        }

        this.addView(view)

        this.setView(-1)
        if (switchToState) {
            this.viewState = state
        }
    }


    fun setViewForState(view: View, state: Int) {
        this.setViewForState(view, state, false)
    }

    fun setViewForState(@LayoutRes layoutRes: Int, state: Int, switchToState: Boolean) {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(this.context)
        }

        val view = this.mInflater!!.inflate(layoutRes, this, false)
        this.setViewForState(view, state, switchToState)
    }

    fun setViewForState(@LayoutRes layoutRes: Int, state: Int) {
        this.setViewForState(layoutRes, state, false)
    }

    fun setAnimateLayoutChanges(animate: Boolean) {
        this.mAnimateViewChanges = animate
    }

    private fun animateLayoutChange(previousView: View?) {
        if (previousView == null) {
            this.getViewByState(this.mViewState)!!.visibility = View.VISIBLE
        } else {
            previousView.visibility = View.VISIBLE
            val anim = ObjectAnimator.ofFloat(previousView, "alpha", 1.0f, 0.0f).setDuration(250L)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    previousView.visibility = View.GONE
                    this@MultiStateView.getViewByState(this@MultiStateView.mViewState)!!.visibility = View.VISIBLE
                    ObjectAnimator.ofFloat(this@MultiStateView.getViewByState(this@MultiStateView.mViewState), "alpha", 0.0f, 1.0f).setDuration(250L).start()
                }
            })
            anim.start()
        }
    }

    interface StateListener {
        fun onStateChanged(var1: Int)
    }


    fun setStateListener(listener: MultiStateView.StateListener) {
        this.mListener = listener
    }

    companion object {
        const val VIEW_STATE_UNKNOWN = -1
        const val VIEW_STATE_CONTENT = 0
        const val VIEW_STATE_ERROR = 1
        const val VIEW_STATE_EMPTY = 2
        const val VIEW_STATE_LOADING = 3
    }
}
