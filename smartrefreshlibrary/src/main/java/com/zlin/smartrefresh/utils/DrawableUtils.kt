package com.zlin.smartrefresh.utils

import com.zlin.smartrefresh.config.BallInfoConfig

object DrawableUtils {

    @JvmStatic
    fun getDrawableSize(ballInfoConfig: BallInfoConfig): FloatArray {
        val mBallRadius = ballInfoConfig.ballRadius
        val mBallHgap = ballInfoConfig.ballHgap
        val mBallVgap = ballInfoConfig.ballVgap
        val mDrawableWidth = 3 * (2 * mBallRadius) + 2 * mBallHgap
        val mDrawableHeight = 2 * mBallRadius + mBallVgap
        return floatArrayOf(mDrawableWidth, mDrawableHeight)
    }

}