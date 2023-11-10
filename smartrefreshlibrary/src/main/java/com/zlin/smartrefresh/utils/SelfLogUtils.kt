package com.zlin.smartrefresh.utils;

import java.util.logging.Level
import java.util.logging.Logger

object SelfLogUtils {

    //是否使能日志
    const val isEnableLog=true

    @JvmStatic
    fun log(tag: String?, msg: String?) {
        if(!isEnableLog){ return }

        val logger = Logger.getLogger(tag)
        logger.log(Level.INFO, msg)
    }


    @JvmStatic
    fun debug(tag: String?, msg: String?) {
        if(!isEnableLog){ return }

        val logger = Logger.getLogger(tag)
        logger.log(Level.INFO, msg)
    }

}