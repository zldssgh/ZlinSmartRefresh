package com.zlin.demo.enumt

import androidx.annotation.StringRes
import com.zlin.demo.R

enum class SmartRefreshLayoutItem(@param:StringRes var nameId: Int) {
    尺寸拉伸(R.string.item_style_spinner_scale),
    位置平移(R.string.item_style_spinner_translation),
    背后固定(R.string.item_style_spinner_behind),
    显示标题(R.string.item_style_spinner_title_on),
    隐藏标题(R.string.item_style_spinner_title_off),
    // 加载更多(R.string.item_style_load_more),
    默认主题(R.string.item_style_theme_default_abstract),
    橙色主题(R.string.item_style_theme_orange_abstract),
    红色主题(R.string.item_style_theme_red_abstract),
    绿色主题(R.string.item_style_theme_green_abstract),
    蓝色主题(R.string.item_style_theme_blue_abstract);
}