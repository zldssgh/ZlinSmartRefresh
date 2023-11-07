package com.zlin.smartrefresh.api.header;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.util.SmartUtil;
import com.zlin.smartrefresh.R;
import com.zlin.smartrefresh.drawable.ThreeBallDrawable;
import com.zlin.smartrefresh.threeball.ThreeBallAbstract;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ThreeBallHeader extends ThreeBallAbstract<ThreeBallHeader> implements RefreshHeader {

    public static String REFRESH_HEADER_PULLING = null;//"下拉可以刷新";
    public static String REFRESH_HEADER_RELEASE = null;//"释放立即刷新";
    public static String REFRESH_HEADER_REFRESHING = null;//"正在刷新...";
    public static String REFRESH_HEADER_FINISH = null;//"刷新完成";
    public static String REFRESH_HEADER_FAILED = null;//"刷新失败";

    protected String mTextPulling;//"下拉可以刷新";
    protected String mTextRelease;//"释放立即刷新";
    protected String mTextRefreshing;//"正在刷新...";
    protected String mTextFinish;//"刷新完成";
    protected String mTextFailed;//"刷新失败";

    public ThreeBallHeader(Context context) {
        this(context, null);
    }

    public ThreeBallHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        //加载布局
        View.inflate(context, R.layout.smartrefresh_layout_header_threeball, this);

        //获取控件
        final View thisView = this;
        final View progressView = mProgressView = thisView.findViewById(R.id.srl_tball_progress);
        mTitleText = thisView.findViewById(R.id.srl_tball_title);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ThreeBallHeader);

        LinearLayout.LayoutParams lpProgress = (LinearLayout.LayoutParams) progressView.getLayoutParams();
        lpProgress.width = ta.getLayoutDimension(R.styleable.ThreeBallHeader_tballDrawableProgressWidth, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(R.styleable.ThreeBallHeader_tballDrawableProgressHeight, lpProgress.height);

        LinearLayout.LayoutParams lpTextTitle = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpTextTitle.topMargin = ta.getDimensionPixelSize(R.styleable.ThreeBallHeader_tballTitleMarginTop, SmartUtil.dp2px(0));
        mTitleText.setLayoutParams(lpTextTitle);

        mFinishDuration = ta.getInt(R.styleable.ThreeBallHeader_tballFinishDuration, mFinishDuration);
        mSpinnerStyle = SpinnerStyle.values[ta.getInt(R.styleable.ThreeBallHeader_tballSpinnerStyle,mSpinnerStyle.ordinal)];

        if (ta.hasValue(R.styleable.ThreeBallHeader_tballDrawableProgressRef)) {
            mProgressView.setImageDrawable(ta.getDrawable(R.styleable.ThreeBallHeader_tballDrawableProgressRef));
        } else if (mProgressView.getDrawable() == null) {
            mProgressDrawable = new ThreeBallDrawable();
            mProgressDrawable.setColor(0xff666666);
            mProgressView.setImageDrawable(mProgressDrawable);
        }

        if (ta.hasValue(R.styleable.ThreeBallHeader_tballTitleTextSize)) {
            mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.ThreeBallHeader_tballTitleTextSize, SmartUtil.dp2px(16)));
        }

        if (ta.hasValue(R.styleable.ThreeBallHeader_tballTitleShowEnable)) {
           if (ta.getBoolean(R.styleable.ThreeBallHeader_tballTitleShowEnable, true)){
               mTitleText.setVisibility(View.VISIBLE);
           }else{
               mTitleText.setVisibility(View.GONE);
           }
        }

        if (ta.hasValue(R.styleable.ThreeBallHeader_tballPrimaryColor)) {
            super.setPrimaryColor(ta.getColor(R.styleable.ThreeBallHeader_tballPrimaryColor, 0));
        }
        if (ta.hasValue(R.styleable.ThreeBallHeader_tballAccentColor)) {
            setAccentColor(ta.getColor(R.styleable.ThreeBallHeader_tballAccentColor, 0));
        }

        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextPulling)){
            mTextPulling = ta.getString(R.styleable.ThreeBallHeader_tballTextPulling);
        } else if(REFRESH_HEADER_PULLING != null) {
            mTextPulling = REFRESH_HEADER_PULLING;
        } else {
            mTextPulling = context.getString(R.string.srl_header_pulling);
        }
        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextRelease)){
            mTextRelease = ta.getString(R.styleable.ThreeBallHeader_tballTextRelease);
        } else if(REFRESH_HEADER_RELEASE != null) {
            mTextRelease = REFRESH_HEADER_RELEASE;
        } else {
            mTextRelease = context.getString(R.string.srl_header_release);
        }
        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextFinish)){
            mTextFinish = ta.getString(R.styleable.ThreeBallHeader_tballTextFinish);
        } else if(REFRESH_HEADER_FINISH != null) {
            mTextFinish = REFRESH_HEADER_FINISH;
        } else {
            mTextFinish = context.getString(R.string.srl_header_finish);
        }
        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextFailed)){
            mTextFailed = ta.getString(R.styleable.ThreeBallHeader_tballTextFailed);
        } else if(REFRESH_HEADER_FAILED != null) {
            mTextFailed = REFRESH_HEADER_FAILED;
        } else {
            mTextFailed = context.getString(R.string.srl_header_failed);
        }

        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextRefreshing)){
            mTextRefreshing = ta.getString(R.styleable.ThreeBallHeader_tballTextRefreshing);
        } else if(REFRESH_HEADER_REFRESHING != null) {
            mTextRefreshing = REFRESH_HEADER_REFRESHING;
        } else {
            mTextRefreshing = context.getString(R.string.srl_header_refreshing);
        }
        ta.recycle();

        progressView.animate().setInterpolator(null);
        mTitleText.setText(thisView.isInEditMode() ? mTextRefreshing : mTextPulling);

        if (thisView.isInEditMode()) {
            //arrowView.setVisibility(GONE);
        } else {
            progressView.setVisibility(GONE);
        }

        try {//try 不能删除-否则会出现兼容性问题
            if (context instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                if (manager != null) {
                    @SuppressLint("RestrictedApi")
                    List<Fragment> fragments = manager.getFragments();
                    if (fragments.size() > 0) {
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (success) {
            mTitleText.setText(mTextFinish);
        } else {
            mTitleText.setText(mTextFailed);
        }
        return super.onFinish(layout, success);//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        Log.e("onStateChanged","oldState="+oldState+"  newState="+newState);
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mTitleText.setText(mTextPulling);
                break;
            case ReleaseToRefresh:
                mTitleText.setText(mTextRelease);
                break;
            case RefreshReleased:
            case Refreshing:
                mTitleText.setText(mTextRefreshing);
                break;
            case ReleaseToTwoLevel:
                break;
            case Loading:
                break;
        }
    }

    public ThreeBallHeader setAccentColor(@ColorInt int accentColor) {
        return super.setAccentColor(accentColor);
    }

}
