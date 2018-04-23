package com.silentgo;

import com.silentgo.danmu.client.douyu.DouyuClient;
import com.silentgo.danmu.client.panda.PandaClient;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

       // DouyuClient douyuClient = new DouyuClient("https://www.douyu.com/158");
        //douyuClient.init();
       // douyuClient.start();


        PandaClient pandaClient = new PandaClient("https://www.panda.tv/7000");
        pandaClient.init();
        pandaClient.start();
    }
}
