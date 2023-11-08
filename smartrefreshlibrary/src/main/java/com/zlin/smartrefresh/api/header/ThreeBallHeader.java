package com.zlin.smartrefresh.api.header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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
import com.zlin.smartrefresh.api.config.BallInfoConfig;
import com.zlin.smartrefresh.api.utils.BallUtils;
import com.zlin.smartrefresh.drawable.ThreeBallDrawable;
import com.zlin.smartrefresh.threeball.ThreeBallAbstract;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ThreeBallHeader extends ThreeBallAbstract<ThreeBallHeader> implements RefreshHeader {

//    public static String REFRESH_HEADER_PULLING = null;//"下拉可以刷新";
//    public static String REFRESH_HEADER_RELEASE = null;//"释放立即刷新";
//    public static String REFRESH_HEADER_REFRESHING = null;//"正在刷新...";
//    public static String REFRESH_HEADER_FINISH = null;//"刷新完成";
//    public static String REFRESH_HEADER_FAILED = null;//"刷新失败";

    protected String mTextPulling;//"下拉可以刷新";
    protected String mTextRelease;//"释放立即刷新";
    protected String mTextRefreshing;//"正在刷新...";
    protected String mTextFinish;//"刷新完成";
    protected String mTextFailed;//"刷新失败";

    protected boolean mTitleShowEnable=true;//标题显示使能
    protected float mBallRadius =0f;//球体半径
    protected float mBallHgap =0f;//球体水平间距
    protected float mBallVgap =0f;//球体垂直间距

    public ThreeBallHeader(Context context) {
        this(context, null);
    }

    public ThreeBallHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        //加载布局
        View.inflate(context, R.layout.smartrefresh_layout_header_threeball, this);

        //获取控件
        final View thisView = this;
        mProgressView = thisView.findViewById(R.id.srl_tball_progress);
        mTitleView = thisView.findViewById(R.id.srl_tball_title);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ThreeBallHeader);

        mProgressDrawable = new ThreeBallDrawable();
        mProgressView.setImageDrawable(mProgressDrawable);

        mBallRadius = ta.getLayoutDimension(R.styleable.ThreeBallHeader_tballBallRadius, SmartUtil.dp2px(getResources().getDimension(R.dimen.tball_ball_radius)));
        mBallHgap = ta.getLayoutDimension(R.styleable.ThreeBallHeader_tballBallHgap, SmartUtil.dp2px(getResources().getDimension(R.dimen.tball_ball_hgap)));
        mBallVgap = ta.getLayoutDimension(R.styleable.ThreeBallHeader_tballBallVgap, SmartUtil.dp2px(getResources().getDimension(R.dimen.tball_ball_hgap)));

        super.setBallRadius(mBallRadius);
        super.setBallHgap(mBallHgap);
        super.setBallVgap(mBallVgap);

        BallInfoConfig ballInfoConfig=new BallInfoConfig();
        ballInfoConfig.setBallRadius(mBallRadius);
        ballInfoConfig.setBallHgap(mBallHgap);
        ballInfoConfig.setBallVgap(mBallVgap);
        float[] mDrawableSize= BallUtils.getDrawableSize(ballInfoConfig);
        ViewGroup.LayoutParams lpProgressView = mProgressView.getLayoutParams();
        lpProgressView.width = (int) mDrawableSize[0];
        lpProgressView.height = (int) mDrawableSize[1];
        mProgressView.setLayoutParams(lpProgressView);

        int titleMarginTop = ta.getDimensionPixelSize(R.styleable.ThreeBallHeader_tballTitleMarginTop, SmartUtil.dp2px(getResources().getDimension(R.dimen.tball_title_margintop)));
        MarginLayoutParams lpTitleView = (MarginLayoutParams) mTitleView.getLayoutParams();
        lpTitleView.topMargin = titleMarginTop;
        mTitleView.setLayoutParams(lpTitleView);

        mSpinnerStyle = SpinnerStyle.values[ta.getInt(R.styleable.ThreeBallHeader_tballSpinnerStyle, mSpinnerStyle.ordinal)];
        mFinishDuration = ta.getInt(R.styleable.ThreeBallHeader_tballFinishDuration, mFinishDuration);

        float titleTextSize=ta.getDimensionPixelSize(R.styleable.ThreeBallHeader_tballTitleTextSize, SmartUtil.dp2px(getResources().getDimension(R.dimen.tball_title_textsize)));
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);

        mTitleShowEnable=ta.getBoolean(R.styleable.ThreeBallHeader_tballTitleShowEnable, mTitleShowEnable);
        if (mTitleShowEnable){
            mTitleView.setVisibility(View.VISIBLE);
        }else{
            mTitleView.setVisibility(View.GONE);
        }

        if (ta.hasValue(R.styleable.ThreeBallHeader_tballPrimaryColor)) {
            super.setPrimaryColor(ta.getColor(R.styleable.ThreeBallHeader_tballPrimaryColor, 0));
        }
        if (ta.hasValue(R.styleable.ThreeBallHeader_tballAccentColor)) {
            super.setAccentColor(ta.getColor(R.styleable.ThreeBallHeader_tballAccentColor, 0));
        }
        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextPulling)){
            mTextPulling = ta.getString(R.styleable.ThreeBallHeader_tballTextPulling);
        } else {
            mTextPulling = context.getString(R.string.tball_header_pulling);
        }
        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextRelease)){
            mTextRelease = ta.getString(R.styleable.ThreeBallHeader_tballTextRelease);
        } else {
            mTextRelease = context.getString(R.string.tball_header_release);
        }
        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextRefreshing)){
            mTextRefreshing = ta.getString(R.styleable.ThreeBallHeader_tballTextRefreshing);
        } else {
            mTextRefreshing = context.getString(R.string.tball_header_refreshing);
        }
        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextFinish)){
            mTextFinish = ta.getString(R.styleable.ThreeBallHeader_tballTextFinish);
        } else {
            mTextFinish = context.getString(R.string.tball_header_finish);
        }
        if(ta.hasValue(R.styleable.ThreeBallHeader_tballTextFailed)){
            mTextFailed = ta.getString(R.styleable.ThreeBallHeader_tballTextFailed);
        } else {
            mTextFailed = context.getString(R.string.tball_header_failed);
        }

        ta.recycle();

        mProgressView.animate().setInterpolator(null);
        mTitleView.setText(thisView.isInEditMode() ? mTextRefreshing : mTextPulling);

        if (thisView.isInEditMode()) {
            //mProgressView.setVisibility(GONE);
        } else {
            //mProgressView.setVisibility(GONE);
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
            mTitleView.setText(mTextFinish);
        } else {
            mTitleView.setText(mTextFailed);
        }
        return super.onFinish(layout, success);//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        Log.e("onStateChanged","oldState="+oldState+"  newState="+newState);

        super.onStateChanged(refreshLayout, oldState, newState);
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mTitleView.setText(mTextPulling);
                break;
            case ReleaseToRefresh:
                mTitleView.setText(mTextRelease);
                break;
            case RefreshReleased:
            case Refreshing:
                mTitleView.setText(mTextRefreshing);
                break;
            case ReleaseToTwoLevel:
                break;
            case Loading:
                break;
        }
    }

}
