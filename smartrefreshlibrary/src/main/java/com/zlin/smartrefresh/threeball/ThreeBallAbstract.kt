package com.zlin.smartrefresh.threeball

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Animatable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.scwang.smart.refresh.layout.api.RefreshComponent
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.scwang.smart.refresh.layout.simple.SimpleComponent
import com.scwang.smart.refresh.layout.util.SmartUtil
import com.zlin.smartrefresh.R
import com.zlin.smartrefresh.drawable.PaintDrawable
import com.zlin.smartrefresh.utils.SelfLogUtils.log

abstract class ThreeBallAbstract<T: ThreeBallAbstract<T>>(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : SimpleComponent(context, attrs, defStyleAttr), RefreshComponent {

    @JvmField
    protected var mProgressView: ImageView? = null
    @JvmField
    protected var mTitleView: TextView? = null

    protected var mRefreshKernel: RefreshKernel? = null

    @JvmField
    protected var mProgressDrawable: PaintDrawable? = null
    protected var mSetPrimaryColor = false
    protected var mSetAccentColor = false
    protected var mPrimaryColor = 0

    @JvmField
    protected var mFinishDuration = 500
    protected var mPaddingTop = 0
    protected var mPaddingBottom = 0
    protected var mMinHeightOfContent = 0

    init {
        mSpinnerStyle = SpinnerStyle.Translate
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val thisView: View = this
        if (mMinHeightOfContent == 0) {
            mPaddingTop = thisView.paddingTop
            mPaddingBottom = thisView.paddingBottom
            if (mPaddingTop == 0 || mPaddingBottom == 0) {
                val paddingLeft = thisView.paddingLeft
                val paddingRight = thisView.paddingRight
                mPaddingTop = if (mPaddingTop == 0) resources.getDimension(R.dimen.tball_header_paddingTop).toInt() else mPaddingTop
                mPaddingBottom = if (mPaddingBottom == 0) resources.getDimension(R.dimen.tball_header_paddingBottom).toInt() else mPaddingBottom
                thisView.setPadding(paddingLeft, mPaddingTop, paddingRight, mPaddingBottom)
            }
            val thisGroup: ViewGroup = this
            thisGroup.clipToPadding = false
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
            if (parentHeight < mMinHeightOfContent) {
                val padding = (parentHeight - mMinHeightOfContent) / 2
                thisView.setPadding(thisView.paddingLeft, padding, thisView.paddingRight, padding)
            } else {
                thisView.setPadding(thisView.paddingLeft, 0, thisView.paddingRight, 0)
            }
        } else {
            thisView.setPadding(thisView.paddingLeft, mPaddingTop, thisView.paddingRight, mPaddingBottom)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mMinHeightOfContent == 0) {
            val thisGroup: ViewGroup = this
            for (i in 0 until thisGroup.childCount) {
                val height = thisGroup.getChildAt(i).measuredHeight
                if (mMinHeightOfContent < height) {
                    mMinHeightOfContent = height
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        log("Animation5", "onDetachedFromWindow....")

        val progressView: View? = mProgressView
        progressView?.animate()?.cancel()
        val drawable = mProgressView?.drawable
        if (drawable is Animatable) {
            if (drawable.isRunning) {
                (drawable as Animatable).stop()
            }
        }
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        mRefreshKernel = kernel
        mRefreshKernel?.requestDrawBackgroundFor(this, mPrimaryColor)
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        log("Animation5", "onStartAnimator(${height}/${maxDragHeight})....")

        if (height==0 && maxDragHeight==0){
            val progressView: View? = mProgressView
            progressView?.visibility = VISIBLE
            val drawable = mProgressView?.drawable
            if (drawable is Animatable) {
                val animatable = drawable as Animatable
                if (!animatable.isRunning) {
                    log("Animation5", "onStartAnimator 有效执行")
                    (drawable as Animatable).start()
                }
            } else {
                //progressView.animate().rotation(36000).setDuration(100000);
            }
        }else{
            log("Animation5", "onStartAnimator 空的执行")
        }
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        if (newState == RefreshState.PullDownToRefresh) {
            onStartAnimator(refreshLayout, 0, 0)
        } else if (newState == RefreshState.None) {
            onStopAnimator()
        }
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return mFinishDuration //延迟500毫秒之后再弹回
    }

    override fun setPrimaryColors(@ColorInt vararg colors: Int) {
        if (colors.isNotEmpty()) {
            val thisView: View = this
            if (thisView.background !is BitmapDrawable && !mSetPrimaryColor) {
                setPrimaryColor(colors[0])
                mSetPrimaryColor = false
            }
            if (!mSetAccentColor) {
                if (colors.size > 1) {
                    setAccentColor(colors[1])
                }
                mSetAccentColor = false
            }
        }
    }

    /**
     * 停止动画
     */
    private fun onStopAnimator() {
        log("Animation5", "onStopAnimator....")

        val progressView: View? = mProgressView
        val drawable = mProgressView?.drawable
        if (drawable is Animatable) {
            val animatable = drawable as Animatable
            if (animatable.isRunning) {
                log("Animation5", "onStopAnimator 有效执行")
                animatable.stop()
            }
        } else {
            //progressView.animate().rotation(0).setDuration(0);
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        log("onSizeChanged", "w/h=$w/$h   oldw/oldh=$oldw/$oldh")
    }

    private fun self(): T {
        return this as T
    }

    fun setSpinnerStyle(style: SpinnerStyle?): T {
        mSpinnerStyle = style
        return self()
    }

    fun setFinishDuration(delay: Int): T {
        mFinishDuration = delay
        return self()
    }

    fun setPrimaryColorId(@ColorRes colorId: Int): T {
        val thisView: View = this
        setPrimaryColor(ContextCompat.getColor(thisView.context, colorId))
        return self()
    }

    fun setAccentColorId(@ColorRes colorId: Int): T {
        val thisView: View = this
        setAccentColor(ContextCompat.getColor(thisView.context, colorId))
        return self()
    }

    fun setCanvasColorId(@ColorRes colorId: Int): T {
        val thisView: View = this
        setCanvasColor(ContextCompat.getColor(thisView.context, colorId))
        return self()
    }

    fun setPrimaryColor(@ColorInt primaryColor: Int): T {
        mSetPrimaryColor = true
        mPrimaryColor = primaryColor
        if (mRefreshKernel != null) {
            mRefreshKernel?.requestDrawBackgroundFor(this, primaryColor)
        }
        return self()
    }

    fun setAccentColor(@ColorInt accentColor: Int): T {
        mSetAccentColor = true
        mTitleView?.setTextColor(accentColor)
        if (mProgressDrawable != null) {
            mProgressDrawable?.setColor(accentColor)
            mProgressView?.invalidateDrawable(mProgressDrawable!!)
        }
        return self()
    }

    fun setCanvasColor(@ColorInt canvasColor: Int): T {
        if (mProgressDrawable != null) {
            mProgressDrawable?.setCanvasColor(canvasColor)
            mProgressView?.invalidateDrawable(mProgressDrawable!!)
        }
        return self()
    }

    fun setBallRadiusDp(dpBallRadius: Float): T {
        return setBallRadiusPx(SmartUtil.dp2px(dpBallRadius).toFloat())
    }

    fun setBallRadiusPx(pxBallRadius: Float): T {
        if (mProgressDrawable != null) {
            mProgressDrawable?.setBallRadius(pxBallRadius)
            mProgressView?.invalidateDrawable(mProgressDrawable!!)
        }
        return self()
    }

    fun setBallHgapDp(pxBallHgap: Float): T {
        return setBallHgapPx(SmartUtil.dp2px(pxBallHgap).toFloat())
    }

    fun setBallHgapPx(pxBallHgap: Float): T {
        if (mProgressDrawable != null) {
            mProgressDrawable?.setBallHgap(pxBallHgap)
            mProgressView?.invalidateDrawable(mProgressDrawable!!)
        }
        return self()
    }

    fun setBallVgapDp(pxBallVgap: Float): T {
        return setBallVgapPx(SmartUtil.dp2px(pxBallVgap).toFloat())
    }

    fun setBallVgapPx(pxBallVgap: Float): T {
        if (mProgressDrawable != null) {
            mProgressDrawable?.setBallVgap(pxBallVgap)
            mProgressView?.invalidateDrawable(mProgressDrawable!!)
        }
        return self()
    }

    fun setProgressBitmap(bitmap: Bitmap?): T {
        mProgressDrawable = null
        mProgressView?.setImageBitmap(bitmap)
        return self()
    }

    fun setProgressDrawable(drawable: Drawable?): T {
        mProgressDrawable = null
        mProgressView?.setImageDrawable(drawable)
        return self()
    }

    fun setProgressResource(@DrawableRes resId: Int): T {
        mProgressDrawable = null
        mProgressView?.setImageResource(resId)
        return self()
    }

    fun setDrawableProgressSizeDp(dpWidth: Float, dpHeight: Float): T {
        return setDrawableProgressSizePx(SmartUtil.dp2px(dpWidth).toFloat(), SmartUtil.dp2px(dpHeight).toFloat())
    }

    fun setDrawableProgressSizePx(pxWidth: Float, pxHeight: Float): T {
        val lpProgressView = mProgressView!!.layoutParams
        lpProgressView.width = pxWidth.toInt()
        lpProgressView.height = pxHeight.toInt()
        mProgressView?.layoutParams = lpProgressView
        return self()
    }

    fun setTextTitleMarginTopDp(dpTop: Float): T {
        return setTextTitleMarginTopPx(SmartUtil.dp2px(dpTop))
    }

    fun setTextTitleMarginTopPx(pxTop: Int): T {
        val lpTitleView = mTitleView?.layoutParams as MarginLayoutParams
        lpTitleView.topMargin = pxTop
        mTitleView?.layoutParams = lpTitleView
        return self()
    }

    fun setTextTitleSizeSp(spTextSize: Float): T {
        return setTextTitleSize(TypedValue.COMPLEX_UNIT_SP, spTextSize)
    }

    fun setTextTitleSize(unit: Int, size: Float): T {
        mTitleView?.setTextSize(unit, size)
        if (mRefreshKernel != null) {
            mRefreshKernel?.requestRemeasureHeightFor(this)
        }
        return self()
    }

    fun setTextBoldEnable(boldEnable: Boolean): T {
        if (boldEnable) {
            mTitleView?.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        } else {
            mTitleView?.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        }
        if (mRefreshKernel != null) {
            mRefreshKernel?.requestRemeasureHeightFor(this)
        }
        return self()
    }

    fun setTextShowEnable(showEnable: Boolean): T {
        if (showEnable) {
            mTitleView?.visibility = VISIBLE
        } else {
            mTitleView?.visibility = GONE
        }
        if (mRefreshKernel != null) {
            mRefreshKernel?.requestRemeasureHeightFor(this)
        }
        return self()
    }

}