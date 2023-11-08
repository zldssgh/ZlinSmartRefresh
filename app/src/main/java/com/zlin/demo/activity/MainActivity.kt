package com.zlin.demo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zlin.demo.R
import com.zlin.demo.adapter.SmartRefreshAdapter
import com.zlin.demo.enumt.SmartRefreshLayoutItem

class MainActivity : AppCompatActivity() {

    private var smartRefreshLayout: SmartRefreshLayout?=null
    private var rv_smart_refresh: RecyclerView?=null

    private var listSmartRefreshLayoutItem: ArrayList<SmartRefreshLayoutItem> = arrayListOf()
    private var smartRefreshAdapter : SmartRefreshAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onInitHandler()
        onEventListenerHandler()
    }

    private fun onInitHandler(){
        smartRefreshLayout=findViewById<SmartRefreshLayout>(R.id.smartRefreshLayout)
        rv_smart_refresh=findViewById<RecyclerView>(R.id.rv_smart_refresh)

        //列表初始化
        listSmartRefreshLayoutItem.clear()
        listSmartRefreshLayoutItem.addAll(SmartRefreshLayoutItem.values())
        smartRefreshAdapter = SmartRefreshAdapter(listSmartRefreshLayoutItem)
        rv_smart_refresh?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = smartRefreshAdapter
        }
    }

    private fun onEventListenerHandler(){
        smartRefreshLayout?.setOnRefreshListener {
            listSmartRefreshLayoutItem.clear()
            listSmartRefreshLayoutItem.addAll(SmartRefreshLayoutItem.values())
            smartRefreshAdapter?.notifyDataSetChanged()

            smartRefreshLayout?.finishRefresh(700)
        }
        smartRefreshLayout?.setOnLoadMoreListener {
            listSmartRefreshLayoutItem.addAll(SmartRefreshLayoutItem.values())
            smartRefreshAdapter?.notifyDataSetChanged()
            smartRefreshLayout?.finishLoadMore(true)
        }
    }
    
}