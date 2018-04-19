package com.silentgo.danmu.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public abstract class DanMuSocket {
    public static final Logger looger = LoggerFactory.getLogger(DanMuSocket.class);

    private Socket socket;

    public DanMuSocket(Socket socket) {
        this.socket = socket;
    }

    public abstract String communicate(String data);

    public abstract void push(String data);

    public abstract String pull();

    public abstract String getDanmu();

    public abstract void keepAlive();

    public void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                looger.error("close socket error", e);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
