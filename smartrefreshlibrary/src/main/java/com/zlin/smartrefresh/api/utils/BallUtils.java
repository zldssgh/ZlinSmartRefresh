package com.zlin.smartrefresh.api.utils;

import com.zlin.smartrefresh.api.config.BallInfoConfig;

public class BallUtils {

    public static float[] getDrawableSize(BallInfoConfig ballInfoConfig){
        float mBallRadius=ballInfoConfig.getBallRadius();
        float mBallHgap=ballInfoConfig.getBallHgap();
        float mBallVgap=ballInfoConfig.getBallVgap();

        float mDrawableWidth=3*(2*mBallRadius)+2*mBallHgap;
        float mDrawableHeight=2*mBallRadius+mBallVgap;

        return new float[]{mDrawableWidth, mDrawableHeight};
    }

}
