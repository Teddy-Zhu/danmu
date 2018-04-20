package com.silentgo;

import com.silentgo.danmu.client.douyu.DouyuClient;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        DouyuClient douyuClient = new DouyuClient("https://www.douyu.com/158");
        douyuClient.init();
        douyuClient.start();

    }
}
