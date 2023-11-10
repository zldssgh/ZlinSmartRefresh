package com.zlin.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zlin.demo.R
import com.zlin.demo.enumt.SmartRefreshLayoutItem

class SmartRefreshAdapter constructor(data:ArrayList<SmartRefreshLayoutItem>) : BaseQuickAdapter<SmartRefreshLayoutItem, BaseViewHolder>(R.layout.item_smart_refresh,data) {

    override fun convert(holder: BaseViewHolder, item: SmartRefreshLayoutItem) {
        with(item){
            holder.setText(R.id.tv_title,"${item.name}")
            holder.setText(R.id.tv_describe,"${context.resources.getString(item.nameId)}")
            holder.setTextColorRes(R.id.tv_describe, R.color.item_describe_color)
        }
    }

}