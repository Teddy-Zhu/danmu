package com.silentgo;

import com.silentgo.danmu.client.douyu.DouyuClient;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        new DouyuClient("https://www.douyu.com/74751").start();


    }
}
