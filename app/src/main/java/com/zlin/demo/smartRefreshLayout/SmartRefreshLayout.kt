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
        .setCanvasColorId(R.color.colorTransparent)
        .setBallRadiusDp(3.9f)
        .setBallHgapDp(12f)
        .setBallVgapDp(5f)
        .setTextTitleMarginTopDp(8f)
        .setTextTitleSizeSp(12f)
        .setTextBoldEnable(false)
        .setTextShowEnable(true)
    setRefreshHeader(threeBallHeader)
}