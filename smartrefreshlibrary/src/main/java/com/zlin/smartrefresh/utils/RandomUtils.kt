package com.zlin.smartrefresh.utils

import java.util.*
import java.util.concurrent.ThreadLocalRandom

object RandomUtils {

    fun randomNumberGenerator(): ArrayList<Int> {
        val listRandom = ArrayList<Int>()
        val n = 3 // 需要生成的不重复随机数的数量
        val maxNumber = 3 // 随机数的上限
        val random = Random()
        for (i in 0 until n) {
            var randomNumber = random.nextInt(maxNumber)
            while (listRandom.contains(randomNumber)) {
                randomNumber = random.nextInt(maxNumber)
            }
            listRandom.add(randomNumber)
        }
        return listRandom
    }

    @JvmStatic
    fun randomInt(sFloat: Float, eFloat: Float): Float {
        val min_val = sFloat.toInt()
        val max_val = eFloat.toInt()
        //        SecureRandom rand = new SecureRandom();
//        rand.setSeed(new Date().getTime());
//        int randomNum = rand.nextInt((max_val - min_val) + 1) + min_val;
        val tlr = ThreadLocalRandom.current()
        val randomNum = tlr.nextInt(min_val, max_val)
        return randomNum.toFloat()
    }

}