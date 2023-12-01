package com.zlin.demo.application

import android.app.Application
import androidx.core.content.ContextCompat
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.zlin.demo.R
import com.zlin.smartrefresh.api.header.ThreeBallHeader

class MyApplication :Application(){

    override fun onCreate() {
        super.onCreate()
    }

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            val isOld=false
            if (isOld){
                val classicsHeader= ClassicsHeader(context)
                //头部最底层背景色
                classicsHeader.setPrimaryColor(ContextCompat.getColor(context.applicationContext, R.color.purple_200))
                //头部移动那块背景色
                //classicsHeader.setBackgroundColor(ContextCompat.getColor(context.applicationContext, R.color.colorTransparent))
                //不显示时间
                classicsHeader.setEnableLastTime(false)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }else{
                ThreeBallHeader(context)
                    .setSpinnerStyle(SpinnerStyle.Translate)
                    .setFinishDuration(0)
                    .setPrimaryColorId(R.color.demo_primary_color)
                    .setAccentColorId(R.color.demo_accent_color)
                    //.setCanvasColorId(R.color.demo_canvas_color)
                    .setBallRadiusDp(3.9f)
                    .setBallHgapDp(8f)
                    .setBallVgapDp(5f)
                    .setTextTitleMarginTopDp(5f)
                    .setTextTitleSizeSp(13.5f)
                    .setTextBoldEnable(true)
                    .setTextShowEnable(true)
            }
        }

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f).setFinishDuration(0)
        }
    }
}