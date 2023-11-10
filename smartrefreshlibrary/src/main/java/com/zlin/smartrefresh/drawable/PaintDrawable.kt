package com.zlin.smartrefresh.drawable

import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.zlin.smartrefresh.config.BallInfoConfig
import com.zlin.smartrefresh.utils.DrawableUtils.getDrawableSize

abstract class PaintDrawable protected constructor() : Drawable() {

    @JvmField
    protected var mPaint = Paint()
    @JvmField
    protected var mCanvasColor = -1
    protected var ballInfoConfig = BallInfoConfig()
    @JvmField
    protected var mBallRadius = 0f
    @JvmField
    protected var mBallHgap = 0f
    protected var mBallVgap = 0f
    protected var mDrawableWidth = 0f
    @JvmField
    protected var mDrawableHeight = 0f

    init {
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaint.color = -0x555556
        //0xff666666
    }

    fun setColor(color: Int) {
        mPaint.color = color
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setCanvasColor(mCanvasColor: Int) {
        this.mCanvasColor = mCanvasColor
    }

    fun setBallRadius(ballRadius: Float) {
        mBallRadius = ballRadius
        updateDrawableSize()
    }

    fun setBallHgap(ballHgap: Float) {
        mBallHgap = ballHgap
        updateDrawableSize()
    }

    fun setBallVgap(ballVgap: Float) {
        mBallVgap = ballVgap
        updateDrawableSize()
    }

    private fun updateDrawableSize() {
        ballInfoConfig.ballRadius = mBallRadius
        ballInfoConfig.ballHgap = mBallHgap
        ballInfoConfig.ballVgap = mBallVgap
        val mDrawableSize = getDrawableSize(ballInfoConfig)
        mDrawableWidth = mDrawableSize[0]
        mDrawableHeight = mDrawableSize[1]
    }
}