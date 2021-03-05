package com.db.homework.utils;

import java.util.Random;

public class RrandomNumber {
    public static String getRandomNumber(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }
}
