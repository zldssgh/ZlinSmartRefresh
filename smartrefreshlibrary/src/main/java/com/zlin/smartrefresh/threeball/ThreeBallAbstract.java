package com.zlin.smartrefresh.threeball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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

    protected int mBackgroundColor;
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
        if (drawable instanceof Animatable) {
            if (((Animatable) drawable).isRunning()) {
                ((Animatable) drawable).stop();
            }
        }
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.requestDrawBackgroundFor(this, mBackgroundColor);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        final View progressView = mProgressView;
        if (progressView.getVisibility() != VISIBLE) {
            progressView.setVisibility(VISIBLE);
            Drawable drawable = mProgressView.getDrawable();
            if ((drawable instanceof Animatable)) {
                ((Animatable) drawable).start();
            } else {
                progressView.animate().rotation(36000).setDuration(100000);
            }
        }
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (newState==RefreshState.PullDownToRefresh){
            onStartAnimator(refreshLayout, 0, 0);
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        //onStartAnimator(refreshLayout, height, maxDragHeight);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        final View progressView = mProgressView;
        Drawable drawable = mProgressView.getDrawable();
        if (drawable instanceof Animatable) {
            if (((Animatable) drawable).isRunning()) {
                ((Animatable) drawable).stop();
            }
        } else {
            progressView.animate().rotation(0).setDuration(0);
        }
        progressView.setVisibility(GONE);
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
        mBackgroundColor = primaryColor;
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

    public T setBallRadius(float ballRadius){
        if (mProgressDrawable != null) {
            mProgressDrawable.setBallRadius(ballRadius);
            mProgressView.invalidateDrawable(mProgressDrawable);
        }
        return self();
    }

    public T setBallHgap(float ballHgap){
        if (mProgressDrawable != null) {
            mProgressDrawable.setBallHgap(ballHgap);
            mProgressView.invalidateDrawable(mProgressDrawable);
        }
        return self();
    }

    public T setBallVgap(float ballVgap){
        if (mProgressDrawable != null) {
            mProgressDrawable.setBallVgap(ballVgap);
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
        ViewGroup.LayoutParams lpProgressView = mProgressView.getLayoutParams();
        lpProgressView.width = SmartUtil.dp2px(dpWidth);
        lpProgressView.height = SmartUtil.dp2px(dpHeight);
        mProgressView.setLayoutParams(lpProgressView);
        return self();
    }

    public T setDrawableProgressSizePx(float pxWidth, float pxHeight) {
        ViewGroup.LayoutParams lpProgressView = mProgressView.getLayoutParams();
        lpProgressView.width = (int) pxWidth;
        lpProgressView.height = (int) pxHeight;
        mProgressView.setLayoutParams(lpProgressView);
        return self();
    }

    public T setTextTitleMarginTop(float dpTop) {
        MarginLayoutParams lpTitleView = (MarginLayoutParams) mTitleView.getLayoutParams();
        lpTitleView.topMargin = SmartUtil.dp2px(dpTop);
        mTitleView.setLayoutParams(lpTitleView);
        return self();
    }

    public T setTextTitleMarginTopPx(int pxTop) {
        MarginLayoutParams lpTitleView = (MarginLayoutParams) mTitleView.getLayoutParams();
        lpTitleView.topMargin = pxTop;
        mTitleView.setLayoutParams(lpTitleView);
        return self();
    }

    public T setTextTitleSize(float size) {
        mTitleView.setTextSize(size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
        }
        return self();
    }

    public T setTextTitleSize(int unit, float size) {
        mTitleView.setTextSize(unit, size);
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

