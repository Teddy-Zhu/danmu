package com.silentgo.danmu.client.douyu;

import cn.hutool.core.thread.ThreadUtil;
import com.silentgo.danmu.base.DanMuSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class DouyuDanMuSocket extends DanMuSocket {
    public static final Logger looger = LoggerFactory.getLogger(DouyuDanMuSocket.class);

    public DouyuDanMuSocket(Socket socket) {
        super(socket);
    }

    @Override
    public String communicate(String data) {
        push(data);
        return pull();
    }

    @Override
    public void push(String data) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {

            int[] pad = new int[]{0xb1, 0x02, 0x00, 0x00};

            int i = 9 + data.length();

            dataOutputStream.write(i);
            dataOutputStream.write(0x00);
            dataOutputStream.write(0x00);
            dataOutputStream.write(0x00);
            dataOutputStream.write(i);
            dataOutputStream.write(0x00);
            dataOutputStream.write(0x00);
            dataOutputStream.write(0x00);

            for (int i1 : pad) {
                dataOutputStream.write(i1);
            }
            dataOutputStream.writeBytes(data);
            dataOutputStream.write(0x00);

            byte[] result = byteArrayOutputStream.toByteArray();
            OutputStream outputStream = getSocket().getOutputStream();
            outputStream.write(result);
            outputStream.flush();

        } catch (IOException e) {

            looger.error("send data error", e);
        } finally {
            try {
                dataOutputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                looger.error("close steam error", e);
            }

        }
    }

    @Override
    public String pull() {
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = getSocket().getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String info;
            while ((info = br.readLine()) != null) {
                stringBuilder.append(info);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            looger.error("pull data error", e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                looger.error("close steam error", e);
            }

        }

        return "";
    }

    @Override
    public String getDanmu() {

        return this.pull();
    }

    @Override
    public void keepAlive() {
        this.push(String.format("type@=keeplive/tick@=%s/", String.valueOf(System.currentTimeMillis())));
        ThreadUtil.safeSleep(30000);
    }
}
