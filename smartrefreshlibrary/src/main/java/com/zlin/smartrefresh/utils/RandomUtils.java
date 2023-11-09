package com.zlin.smartrefresh.utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static   ArrayList<Integer>  randomNumberGenerator () {
        ArrayList<Integer> listRandom=new ArrayList<>();

        int n = 3; // 需要生成的不重复随机数的数量
        int maxNumber = 3; // 随机数的上限

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int randomNumber = random.nextInt(maxNumber);
            while (listRandom.contains(randomNumber)) {
                randomNumber = random.nextInt(maxNumber);
            }
            listRandom.add(randomNumber);
        }

        return listRandom;
    }

    public static float randomInt(float sFloat, float eFloat){
        int min_val = (int) sFloat;
        int max_val = (int) eFloat;
//        SecureRandom rand = new SecureRandom();
//        rand.setSeed(new Date().getTime());
//        int randomNum = rand.nextInt((max_val - min_val) + 1) + min_val;

        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        int randomNum = tlr.nextInt(min_val, max_val );
        return randomNum;
    }

}
