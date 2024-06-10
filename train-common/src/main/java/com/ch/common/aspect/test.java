package com.ch.common.aspect;

import com.ch.common.utils.JwtUtil;

/**
 * author: ch
 * create: 2024--109:57
 * Description:
 */
public class test {
    public static void main(String[] args) {
        System.out.println(JwtUtil.createToken(1L, ""));
    }
}
