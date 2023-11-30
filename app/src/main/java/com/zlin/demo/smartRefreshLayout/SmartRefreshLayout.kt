package com.zlin.demo.smartRefreshLayout

import android.content.Context
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.zlin.demo.R
import com.zlin.smartrefresh.api.header.ThreeBallHeader

fun SmartRefreshLayout.setThreeBallHeader(context: Context){
    val threeBallHeader=ThreeBallHeader(context)
        .setSpinnerStyle(SpinnerStyle.Translate)
        .setFinishDuration(0)
        .setPrimaryColorId(R.color.colorTransparent)
        .setAccentColorId(R.color.srlcolor_ABAAADBD)
        //.setCanvasColorId(R.color.demo_canvas_color)
        .setBallRadiusDp(3.9f)
        .setBallHgapDp(8f)
        .setBallVgapDp(5f)
        .setTextTitleMarginTopDp(5f)
        .setTextTitleSizeSp(13.5f)
        .setTextBoldEnable(true)
        .setTextShowEnable(true)
    setRefreshHeader(threeBallHeader)
}