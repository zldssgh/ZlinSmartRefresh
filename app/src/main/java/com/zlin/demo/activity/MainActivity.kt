package com.zlin.demo.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.zlin.demo.R
import com.zlin.demo.adapter.SmartRefreshAdapter
import com.zlin.demo.enumt.SmartRefreshLayoutItem
import com.zlin.smartrefresh.api.header.ThreeBallHeader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var smartRefreshLayout: SmartRefreshLayout?=null
    private var rv_smart_refresh: RecyclerView?=null

    private var threeBallHeader:ThreeBallHeader?=null
    private var drawableProgress: Drawable?=null

    private var listSmartRefreshLayoutItem: ArrayList<SmartRefreshLayoutItem> = arrayListOf()
    private var smartRefreshAdapter : SmartRefreshAdapter? = null

    private var isFirstEnter = true

    private var isAppReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //延缓启动画面的时长
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (isAppReady) {
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                }
                return isAppReady
            }
        })
        delayBootTime()

        onExitAnimation()

        onInitHandler()
        onEventListenerHandler()
    }

    private fun delayBootTime() {
        lifecycleScope.launch {
            delay(1000)
            isAppReady = true
        }
    }

    private fun onExitAnimation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val slideUp = ObjectAnimator.ofFloat(splashScreenView, View.TRANSLATION_Y, 0f, -splashScreenView.height.toFloat())
                slideUp.duration = 2000
                // 在自定义动画结束时调用splashScreenView.remove()
                slideUp.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                        splashScreenView.remove()
                    }
                })
                slideUp.start()
            }
        }
    }

    private fun onInitHandler(){
        smartRefreshLayout=findViewById<SmartRefreshLayout>(R.id.smartRefreshLayout)
        rv_smart_refresh=findViewById<RecyclerView>(R.id.rv_smart_refresh)

        //获取ThreeBallHeader
        threeBallHeader=smartRefreshLayout?.refreshHeader as? ThreeBallHeader

        val a=threeBallHeader?.findViewById(ThreeBallHeader.ID_IMAGE_PROGRESS) as? View
        //获取Drawable
        drawableProgress = (threeBallHeader?.findViewById(ThreeBallHeader.ID_IMAGE_PROGRESS) as ImageView).drawable
        if (drawableProgress is LayerDrawable) {
            drawableProgress = (drawableProgress as LayerDrawable).getDrawable(0)
        }

        //列表初始化
        listSmartRefreshLayoutItem.clear()
        listSmartRefreshLayoutItem.addAll(SmartRefreshLayoutItem.values())
        smartRefreshAdapter = SmartRefreshAdapter(listSmartRefreshLayoutItem)
        rv_smart_refresh?.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = smartRefreshAdapter
        }

        //首次进入自动刷新
        if (isFirstEnter) {
            isFirstEnter = false
            //触发自动刷新
            smartRefreshLayout?.autoRefresh()
        }
    }

    private fun onEventListenerHandler(){
        smartRefreshLayout?.setOnRefreshListener {
            listSmartRefreshLayoutItem.clear()
            listSmartRefreshLayoutItem.addAll(SmartRefreshLayoutItem.values())
            smartRefreshAdapter?.notifyDataSetChanged()

            smartRefreshLayout?.finishRefresh(3000)
        }
        smartRefreshLayout?.setOnLoadMoreListener {
            listSmartRefreshLayoutItem.addAll(SmartRefreshLayoutItem.values())
            smartRefreshAdapter?.notifyDataSetChanged()
            smartRefreshLayout?.finishLoadMore(true)
        }

        smartRefreshAdapter?.setOnItemClickListener { adapter, view, position ->
            //获取SmartRefreshLayoutItem
            val smartRefreshLayoutItem=smartRefreshAdapter?.getItem(position)

            //根据SmartRefreshLayoutItem进行相关处理
            when(smartRefreshLayoutItem){
                SmartRefreshLayoutItem.尺寸拉伸->{
                    threeBallHeader?.setSpinnerStyle(SpinnerStyle.values[1])
                }
                SmartRefreshLayoutItem.位置平移->{
                    threeBallHeader?.setSpinnerStyle(SpinnerStyle.Translate)
                }
                SmartRefreshLayoutItem.背后固定->{
                    threeBallHeader?.setSpinnerStyle(SpinnerStyle.FixedBehind)
                    smartRefreshLayout?.setPrimaryColors(-0xbbbbbc, -0x1)
                    if (Build.VERSION.SDK_INT >= 21) {
                        drawableProgress?.setTint(-0x1)
                    } else if (drawableProgress is VectorDrawableCompat) {
                        (drawableProgress as VectorDrawableCompat).setTint(-0x1)
                    }
                    /*
                     * 由于是后面才设置，需要手动更改视图的位置
                     * 如果在 onCreate 或者 xml 中设置好[SpinnerStyle] 就不用手动调整位置了
                     */
                    smartRefreshLayout?.layout?.bringChildToFront(rv_smart_refresh)
                }


                SmartRefreshLayoutItem.显示标题->{
                    threeBallHeader?.setTextShowEnable(true)
                }
                SmartRefreshLayoutItem.隐藏标题->{
                    threeBallHeader?.setTextShowEnable(false)
                }


                SmartRefreshLayoutItem.默认主题->{
                    setThemeColor(R.color.colorPrimary, R.color.colorPrimaryDark)
                    smartRefreshLayout?.layout?.setBackgroundResource(android.R.color.transparent)
                    smartRefreshLayout?.setPrimaryColors(0, -0x99999a)
                    if (Build.VERSION.SDK_INT >= 21) {
                        drawableProgress?.setTint(-0x99999a)
                    } else if (drawableProgress is VectorDrawableCompat) {
                        (drawableProgress as VectorDrawableCompat).setTint(-0x99999a)
                    }
                }
                SmartRefreshLayoutItem.蓝色主题->{
                    setThemeColor(R.color.colorPrimary, R.color.colorPrimaryDark)
                }
                SmartRefreshLayoutItem.绿色主题->{
                    setThemeColor(android.R.color.holo_green_light, android.R.color.holo_green_dark)
                }
                SmartRefreshLayoutItem.红色主题->{
                    setThemeColor(android.R.color.holo_red_light, android.R.color.holo_red_dark)
                }
                SmartRefreshLayoutItem.橙色主题->{
                    setThemeColor(android.R.color.holo_orange_light, android.R.color.holo_orange_dark)
                }
            }

            //自动刷新
            smartRefreshLayout?.autoRefresh()
        }
    }

    private fun setThemeColor(colorPrimary: Int, colorPrimaryDark: Int) {
        smartRefreshLayout?.setPrimaryColorsId(colorPrimary, android.R.color.white)
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, colorPrimaryDark)
            drawableProgress?.setTint(-0x1)
        } else if (drawableProgress is VectorDrawableCompat) {
            (drawableProgress as VectorDrawableCompat).setTint(-0x1)
        }
    }
    
}