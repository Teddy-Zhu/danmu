package com.silentgo.danmu.base;

import com.silentgo.danmu.netty.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public abstract class DanMuSocket {
    public static final Logger logger = LoggerFactory.getLogger(DanMuSocket.class);

    private NettyClient nettyClient;

    public DanMuSocket(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public abstract void push(String data);

    public abstract void resolveMsg(String msg);

    public abstract void keepAlive();

    public void close() {
        logger.info("enter close douyu socket  close");

        if (nettyClient != null) {
            nettyClient.close();
        }
    }

    public NettyClient getNettyClient() {
        return nettyClient;
    }
}
