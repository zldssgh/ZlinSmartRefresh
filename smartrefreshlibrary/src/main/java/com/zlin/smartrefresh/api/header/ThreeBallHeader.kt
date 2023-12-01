package com.zlin.smartrefresh.api.header

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.zlin.smartrefresh.R
import com.zlin.smartrefresh.drawable.ThreeBallDrawable
import com.zlin.smartrefresh.threeball.ThreeBallAbstract
import com.zlin.smartrefresh.utils.SelfLogUtils.log

class ThreeBallHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ThreeBallAbstract<ThreeBallHeader>(context, attrs, 0), RefreshHeader {
    
    //    public static String REFRESH_HEADER_PULLING = null;//"下拉可以刷新";
    //    public static String REFRESH_HEADER_RELEASE = null;//"释放立即刷新";
    //    public static String REFRESH_HEADER_REFRESHING = null;//"正在刷新...";
    //    public static String REFRESH_HEADER_FINISH = null;//"刷新完成";
    //    public static String REFRESH_HEADER_FAILED = null;//"刷新失败";
    
    private var mTextPulling: String? = null //"下拉可以刷新";
    private var mTextRelease: String? = null //"释放立即刷新";
    private var mTextRefreshing: String? = null //"正在刷新...";
    private var mTextFinish: String? = null //"刷新完成";
    private var mTextFailed: String? = null //"刷新失败";

//    private var mBallRadius = 0f //球体半径
//    private var mBallHgap = 0f //球体水平间距
//    private var mBallVgap = 0f //球体垂直间距

    private var mTitleBoldEnable = false //标题粗体使能
    private var mTitleShowEnable = true //标题显示使能

    init {
        //加载布局
        inflate(context, R.layout.smartrefresh_layout_header_three_ball, this)

        //获取控件
        val thisView: View = this
        mProgressView = thisView.findViewById(R.id.srl_tball_progress)
        mTitleView = thisView.findViewById(R.id.srl_tball_title)

        //获取TypedArray
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ThreeBallHeader)

        //获取BallInfoConfig的参数
        val mBallRadius = ta.getDimension(R.styleable.ThreeBallHeader_tballBallRadius, resources.getDimensionPixelSize(R.dimen.tball_ball_radius).toFloat())
        val mBallHgap = ta.getDimension(R.styleable.ThreeBallHeader_tballBallHgap, resources.getDimensionPixelSize(R.dimen.tball_ball_hgap).toFloat())
        val mBallVgap = ta.getDimension(R.styleable.ThreeBallHeader_tballBallVgap, resources.getDimensionPixelSize(R.dimen.tball_ball_vgap).toFloat())

        //配置BallInfoConfig
        ballInfoConfig.ballRadius = mBallRadius
        ballInfoConfig.ballHgap = mBallHgap
        ballInfoConfig.ballVgap = mBallVgap

        //设置mProgressView大小
        super.updateBallInfoConfigAndProgressSize()

        //创建ThreeBallDrawable
        mProgressDrawable = ThreeBallDrawable()
        mProgressDrawable?.updateBallInfoConfig(ballInfoConfig)
        mProgressView?.setImageDrawable(mProgressDrawable)

        val titleMarginTop = ta.getDimension(R.styleable.ThreeBallHeader_tballTitleMarginTop, resources.getDimension(R.dimen.tball_title_margintop))
        val lpTitleView = mTitleView?.layoutParams as MarginLayoutParams
        lpTitleView.topMargin = titleMarginTop.toInt()
        mTitleView?.layoutParams = lpTitleView
        
        mSpinnerStyle = SpinnerStyle.values[ta.getInt(R.styleable.ThreeBallHeader_tballSpinnerStyle, mSpinnerStyle.ordinal)]
        mFinishDuration = ta.getInt(R.styleable.ThreeBallHeader_tballFinishDuration, mFinishDuration)
        
        val titleTextSize = ta.getDimension(R.styleable.ThreeBallHeader_tballTitleTextSize, resources.getDimension(R.dimen.tball_title_textsize))
        mTitleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)

        val titleBoldEnable = ta.getBoolean(R.styleable.ThreeBallHeader_tballTitleBoldEnable, mTitleBoldEnable)
        if (titleBoldEnable) {
            mTitleView?.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        } else {
            mTitleView?.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        }

        val titleShowEnable = ta.getBoolean(R.styleable.ThreeBallHeader_tballTitleShowEnable, mTitleShowEnable)
        if (titleShowEnable) {
            mTitleView?.setVisibility(VISIBLE)
        } else {
            mTitleView?.setVisibility(GONE)
        }

        if (ta.hasValue(R.styleable.ThreeBallHeader_tballPrimaryColor)) {
            super.setPrimaryColor(ta.getColor(R.styleable.ThreeBallHeader_tballPrimaryColor, 0))
        }
        if (ta.hasValue(R.styleable.ThreeBallHeader_tballAccentColor)) {
            super.setAccentColor(ta.getColor(R.styleable.ThreeBallHeader_tballAccentColor, 0))
        }
        if (ta.hasValue(R.styleable.ThreeBallHeader_tballCanvasColor)) {
            super.setCanvasColor(ta.getColor(R.styleable.ThreeBallHeader_tballCanvasColor, 0))
        }
        mTextPulling = if (ta.hasValue(R.styleable.ThreeBallHeader_tballTextPulling)) {
            ta.getString(R.styleable.ThreeBallHeader_tballTextPulling)
        } else {
            context.getString(R.string.tball_header_pulling)
        }
        mTextRelease = if (ta.hasValue(R.styleable.ThreeBallHeader_tballTextRelease)) {
            ta.getString(R.styleable.ThreeBallHeader_tballTextRelease)
        } else {
            context.getString(R.string.tball_header_release)
        }
        mTextRefreshing = if (ta.hasValue(R.styleable.ThreeBallHeader_tballTextRefreshing)) {
            ta.getString(R.styleable.ThreeBallHeader_tballTextRefreshing)
        } else {
            context.getString(R.string.tball_header_refreshing)
        }
        mTextFinish = if (ta.hasValue(R.styleable.ThreeBallHeader_tballTextFinish)) {
            ta.getString(R.styleable.ThreeBallHeader_tballTextFinish)
        } else {
            context.getString(R.string.tball_header_finish)
        }
        mTextFailed = if (ta.hasValue(R.styleable.ThreeBallHeader_tballTextFailed)) {
            ta.getString(R.styleable.ThreeBallHeader_tballTextFailed)
        } else {
            context.getString(R.string.tball_header_failed)
        }

        ta.recycle()
        mProgressView?.animate()?.setInterpolator(null)

        mTitleView?.text = if (thisView.isInEditMode) mTextRefreshing else mTextPulling
        
        if (thisView.isInEditMode) {
            //mProgressView.setVisibility(GONE);
        } else {
            //mProgressView.setVisibility(GONE);
        }
        
        try { //try 不能删除-否则会出现兼容性问题
            if (context is FragmentActivity) {
                val manager = context.supportFragmentManager
                if (manager != null) {
                    @SuppressLint("RestrictedApi") val fragments = manager.fragments
                    if (fragments.size > 0) {
                        //return
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        if (success) {
            mTitleView?.text = mTextFinish
        } else {
            mTitleView?.text = mTextFailed
        }
        return super.onFinish(layout, success) //延迟500毫秒之后再弹回
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        log("onStateChanged", "oldState=$oldState  newState=$newState")

        super.onStateChanged(refreshLayout, oldState, newState)
        when (newState) {
            RefreshState.None, RefreshState.PullDownToRefresh -> {
                mTitleView?.text = mTextPulling
            }
            RefreshState.ReleaseToRefresh -> {
                mTitleView?.text = mTextRelease
            }
            RefreshState.RefreshReleased, RefreshState.Refreshing -> {
                mTitleView?.text = mTextRefreshing
            }
            RefreshState.ReleaseToTwoLevel -> {

            }
            RefreshState.Loading -> {

            }
        }
    }

    companion object {
        val ID_IMAGE_PROGRESS = R.id.srl_tball_progress
        val ID_TEXT_TITLE = R.id.srl_tball_title
    }

}