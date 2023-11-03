package com.zlin.demo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zlin.demo.R
import com.zlin.smartrefresh.header.MyRefreshHeader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyRefreshHeader().onShow()
    }
    
}