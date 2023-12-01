package com.zlin.smartrefresh.drawable

import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.zlin.smartrefresh.config.BallInfoConfig

abstract class PaintDrawable protected constructor() : Drawable() {

    @JvmField
    protected var mPaint = Paint()
    @JvmField
    protected var mCanvasColor = -1

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

    open fun updateBallInfoConfig(ballInfoConfig: BallInfoConfig) {
        mBallRadius = ballInfoConfig.ballRadius
        mBallHgap = ballInfoConfig.ballHgap
        mBallVgap = ballInfoConfig.ballVgap
        mDrawableWidth = ballInfoConfig.drawableWidth
        mDrawableHeight = ballInfoConfig.drawableHeight
    }

}