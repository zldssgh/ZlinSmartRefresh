package com.zlin.smartrefresh.drawable;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public abstract class PaintDrawable extends Drawable {

    protected Paint mPaint = new Paint();

    protected float mBallRadius =0f;
    protected float mBallHgap =0f;
    protected float mBallVgap =0f;

    protected float mDrawableWidth =0f;
    protected float mDrawableHeight =0f;

    protected PaintDrawable() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xffaaaaaa);
        //0xff666666
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setBallRadius(float ballRadius){
        this.mBallRadius=ballRadius;
        updateDrawableSize();
    }

    public void setBallHgap(float ballHgap){
        this.mBallHgap=ballHgap;
        updateDrawableSize();
    }

    public void setBallVgap(float ballVgap){
        this.mBallVgap=ballVgap;
        updateDrawableSize();
    }

    private void updateDrawableSize(){
        mDrawableWidth=3*(2*mBallRadius)+2*mBallHgap;
        mDrawableHeight=2*mBallRadius+mBallVgap;
    }

}
