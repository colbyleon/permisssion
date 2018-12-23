package com.idreamsky.permission.util;

import org.junit.Test;

import java.util.Random;

/**
 * @Author: colby
 * @Date: 2018/12/19 22:32
 */
public class PasswordUtil {
    public static final String[] word = {
            "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    public static final String[] num = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };

    public static String randomPassword() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        boolean flag = false;
        int length = random.nextInt(3) + 8;
        for (int i = 0; i < length; i++) {
            if (flag) {
                sb.append(num[random.nextInt(num.length)]);
            } else {
                sb.append(word[random.nextInt(word.length)]);
            }
            flag = !flag;
        }
        return shuffle(sb.toString());
    }

    private static String shuffle(String str) {
        char[] chars = str.toCharArray();
        Random random = new Random();
        for (int i = chars.length - 1; i > 0; i--) {
            char temp = chars[i];
            int index = random.nextInt(i);
            chars[i] = chars[index];
            chars[index] = temp;
        }
        return new String(chars);
    }

    @Test
    public void testPassword() {
        System.out.println(randomPassword());
        System.out.println(randomPassword());
        System.out.println(randomPassword());
        System.out.println(randomPassword());
        System.out.println(randomPassword());
        System.out.println(randomPassword());
    }
}
