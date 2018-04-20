package com.silentgo.danmu.base;

public interface DanMuClient {

    public void init();

    public void start();

    public void resolveMsg(BaseMsg msg);

}
