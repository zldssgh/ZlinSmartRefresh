package com.zlin.smartrefresh.threeball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.scwang.smart.refresh.layout.api.RefreshComponent;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.simple.SimpleComponent;
import com.scwang.smart.refresh.layout.util.SmartUtil;
import com.zlin.smartrefresh.R;
import com.zlin.smartrefresh.drawable.PaintDrawable;
import com.zlin.smartrefresh.drawable.ThreeBallDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import static android.view.View.MeasureSpec.EXACTLY;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class ThreeBallAbstract<T extends ThreeBallAbstract> extends SimpleComponent implements RefreshComponent {

    public static final int ID_IMAGE_PROGRESS = R.id.srl_tball_progress;
    public static final int ID_TEXT_TITLE = R.id.srl_tball_title;

    protected ImageView mProgressView;
    protected TextView mTitleView;

    protected RefreshKernel mRefreshKernel;

    protected PaintDrawable mProgressDrawable;

    protected boolean mSetPrimaryColor;
    protected boolean mSetAccentColor;

    protected int mPrimaryColor;
    protected int mFinishDuration = 500;
    protected int mPaddingTop = 0;
    protected int mPaddingBottom = 0;
    protected int mMinHeightOfContent = 0;

    public ThreeBallAbstract(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSpinnerStyle = SpinnerStyle.Translate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final View thisView = this;
        if (mMinHeightOfContent == 0) {
            mPaddingTop = thisView.getPaddingTop();
            mPaddingBottom = thisView.getPaddingBottom();
            if (mPaddingTop == 0 || mPaddingBottom == 0) {
                int paddingLeft = thisView.getPaddingLeft();
                int paddingRight = thisView.getPaddingRight();
                mPaddingTop = mPaddingTop == 0 ? SmartUtil.dp2px(getResources().getDimension(R.dimen.tball_header_paddingTop)) : mPaddingTop;
                mPaddingBottom = mPaddingBottom == 0 ? SmartUtil.dp2px(getResources().getDimension(R.dimen.tball_header_paddingBottom)) : mPaddingBottom;
                thisView.setPadding(paddingLeft, mPaddingTop, paddingRight, mPaddingBottom);
            }
            final ViewGroup thisGroup = this;
            thisGroup.setClipToPadding(false);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == EXACTLY) {
            final int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            if (parentHeight < mMinHeightOfContent) {
                final int padding = (parentHeight - mMinHeightOfContent) / 2;
                thisView.setPadding(thisView.getPaddingLeft(), padding, thisView.getPaddingRight(), padding);
            } else {
                thisView.setPadding(thisView.getPaddingLeft(), 0, thisView.getPaddingRight(), 0);
            }
        } else {
            thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop, thisView.getPaddingRight(), mPaddingBottom);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMinHeightOfContent == 0) {
            final ViewGroup thisGroup = this;
            for (int i = 0; i < thisGroup.getChildCount(); i++) {
                final int height = thisGroup.getChildAt(i).getMeasuredHeight();
                if (mMinHeightOfContent < height) {
                    mMinHeightOfContent = height;
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        final View progressView = mProgressView;
        progressView.animate().cancel();
        final Drawable drawable = mProgressView.getDrawable();
        if (drawable instanceof ThreeBallDrawable) {
            if (((ThreeBallDrawable) drawable).isRunning()) {
                ((ThreeBallDrawable) drawable).closeTask();
            }
        }
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.requestDrawBackgroundFor(this, mPrimaryColor);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        final View progressView = mProgressView;
        progressView.setVisibility(VISIBLE);
        Drawable drawable = mProgressView.getDrawable();
        if ((drawable instanceof Animatable)) {
            ((Animatable) drawable).start();
        } else {
            //progressView.animate().rotation(36000).setDuration(100000);
        }
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (newState==RefreshState.PullDownToRefresh){
            onStartAnimator(refreshLayout, 0, 0);
        }else if (newState==RefreshState.None){
            onStopAnimator();
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        //onStartAnimator(refreshLayout, height, maxDragHeight);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return mFinishDuration;//延迟500毫秒之后再弹回
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {
        if (colors.length > 0) {
            final View thisView = this;
            if (!(thisView.getBackground() instanceof BitmapDrawable) && !mSetPrimaryColor) {
                setPrimaryColor(colors[0]);
                mSetPrimaryColor = false;
            }
            if (!mSetAccentColor) {
                if (colors.length > 1) {
                    setAccentColor(colors[1]);
                }
                mSetAccentColor = false;
            }
        }
    }

    /**
     * 停止动画
     */
    private void onStopAnimator(){
        final View progressView = mProgressView;
        Drawable drawable = mProgressView.getDrawable();
        if (drawable instanceof Animatable) {
            Animatable animatable = (Animatable) drawable;
            if ((animatable).isRunning()) {
                animatable.stop();
            }
        } else {
            //progressView.animate().rotation(0).setDuration(0);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.e("onSizeChanged","w/h="+w+"/"+h+"   oldw/oldh="+oldw+"/"+oldh);
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    public T setSpinnerStyle(SpinnerStyle style) {
        this.mSpinnerStyle = style;
        return self();
    }

    public T setFinishDuration(int delay) {
        mFinishDuration = delay;
        return self();
    }

    public T setPrimaryColorId(@ColorRes int colorId) {
        final View thisView = this;
        setPrimaryColor(ContextCompat.getColor(thisView.getContext(), colorId));
        return self();
    }

    public T setAccentColorId(@ColorRes int colorId) {
        final View thisView = this;
        setAccentColor(ContextCompat.getColor(thisView.getContext(), colorId));
        return self();
    }

    public T setPrimaryColor(@ColorInt int primaryColor) {
        mSetPrimaryColor = true;
        mPrimaryColor = primaryColor;
        if (mRefreshKernel != null) {
            mRefreshKernel.requestDrawBackgroundFor(this, primaryColor);
        }
        return self();
    }

    public T setAccentColor(@ColorInt int accentColor) {
        mSetAccentColor = true;
        mTitleView.setTextColor(accentColor);
        if (mProgressDrawable != null) {
            mProgressDrawable.setColor(accentColor);
            mProgressView.invalidateDrawable(mProgressDrawable);
        }
        return self();
    }

    public T setBallRadiusDp(float dpBallRadius){
        return setBallRadiusPx(SmartUtil.dp2px(dpBallRadius));
    }

    public T setBallRadiusPx(float pxBallRadius){
        if (mProgressDrawable != null) {
            mProgressDrawable.setBallRadius(pxBallRadius);
            mProgressView.invalidateDrawable(mProgressDrawable);
        }


        return self();
    }

    public T setBallHgapDp(float pxBallHgap){
        return setBallHgapPx(SmartUtil.dp2px(pxBallHgap));
    }

    public T setBallHgapPx(float pxBallHgap){
        if (mProgressDrawable != null) {
            mProgressDrawable.setBallHgap(pxBallHgap);
            mProgressView.invalidateDrawable(mProgressDrawable);
        }


        return self();
    }

    public T setBallVgapDp(float pxBallVgap){
        return setBallVgapPx(SmartUtil.dp2px(pxBallVgap));
    }

    public T setBallVgapPx(float pxBallVgap){
        if (mProgressDrawable != null) {
            mProgressDrawable.setBallVgap(pxBallVgap);
            mProgressView.invalidateDrawable(mProgressDrawable);
        }

        return self();
    }

    public T setProgressBitmap(Bitmap bitmap) {
        mProgressDrawable = null;
        mProgressView.setImageBitmap(bitmap);
        return self();
    }

    public T setProgressDrawable(Drawable drawable) {
        mProgressDrawable = null;
        mProgressView.setImageDrawable(drawable);
        return self();
    }

    public T setProgressResource(@DrawableRes int resId) {
        mProgressDrawable = null;
        mProgressView.setImageResource(resId);
        return self();
    }

    public T setDrawableProgressSizeDp(float dpWidth, float dpHeight) {
        return setDrawableProgressSizePx(SmartUtil.dp2px(dpWidth),  SmartUtil.dp2px(dpHeight));
    }

    public T setDrawableProgressSizePx(float pxWidth, float pxHeight) {
        ViewGroup.LayoutParams lpProgressView = mProgressView.getLayoutParams();
        lpProgressView.width = (int) pxWidth;
        lpProgressView.height = (int) pxHeight;
        mProgressView.setLayoutParams(lpProgressView);
        return self();
    }

    public T setTextTitleMarginTopDp(float dpTop) {
        return setTextTitleMarginTopPx(SmartUtil.dp2px(dpTop));
    }

    public T setTextTitleMarginTopPx(int pxTop) {
        MarginLayoutParams lpTitleView = (MarginLayoutParams) mTitleView.getLayoutParams();
        lpTitleView.topMargin = pxTop;
        mTitleView.setLayoutParams(lpTitleView);
        return self();
    }

    public T setTextTitleSizeSp(float spTextSize) {
        return setTextTitleSize(TypedValue.COMPLEX_UNIT_SP, spTextSize);
    }

    public T setTextTitleSize(int unit, float size) {
        mTitleView.setTextSize(unit, size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
        }
        return self();
    }

    public T setTextBoldEnable(boolean boldEnable){
        if (boldEnable){
            mTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else{
            mTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
        }
        return self();
    }

    public T setTextShowEnable(boolean showEnable) {
        if (showEnable){
            mTitleView.setVisibility(View.VISIBLE);
        }else{
            mTitleView.setVisibility(View.GONE);
        }
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
        }
        return self();
    }

}

