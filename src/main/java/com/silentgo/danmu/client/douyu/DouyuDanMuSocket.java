package com.silentgo.danmu.client.douyu;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.silentgo.danmu.base.DanMuSocket;
import com.silentgo.danmu.netty.NettyClient;
import com.silentgo.danmu.netty.NettyMsgResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DouyuDanMuSocket extends DanMuSocket {
    public static final Logger logger = LoggerFactory.getLogger(DouyuDanMuSocket.class);


    public DouyuDanMuSocket(NettyClient nettyClient) {

        super(nettyClient);
        DouyuDanMuSocket douyuDanMuSocket = this;
        nettyClient.setMsgResolver(new NettyMsgResolver() {
            @Override
            public void resolveMsg(String msg) {
                logger.info("enter resolveMsg resolver");
                douyuDanMuSocket.resolveMsg(msg);
            }
        });
    }

    @Override
    public void push(String data) {
        logger.info("enter douyu push data");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            int i = 4 + 4 + 1 + data.length();

            dataOutputStream.write(i);
            dataOutputStream.write(0x00);
            dataOutputStream.write(0x00);
            dataOutputStream.write(0x00);
            dataOutputStream.write(i);
            dataOutputStream.write(0x00);
            dataOutputStream.write(0x00);
            dataOutputStream.write(0x00);

            for (int i1 : DouyuClient.douyuSendMark) {
                dataOutputStream.write(i1);
            }
            dataOutputStream.writeBytes(data);
            dataOutputStream.write(0x00);

            byte[] result = byteArrayOutputStream.toByteArray();
            getNettyClient().write(result);


        } catch (IOException e) {

            logger.error("send data error", e);
        } finally {
            try {
                dataOutputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                logger.error("close steam error", e);
            }

        }
    }

    @Override
    public void resolveMsg(String content) {
        logger.info("enter douyu resolve msg");

        List<String> msgs = ReUtil.findAll("(type@=.*?)\\x00", content, 0, new ArrayList<>());
        for (String msg : msgs) {
            msg = msg.replace("@=", "\":\"").replace("/", "\",\"")
                    .replace("@A", "@").replace("@S", "/");
            JSONObject jsonObject = JSONUtil.parseObj("{" + msg.substring(0, msg.length() - 2) + "}");

            logger.info("get msg:{}", jsonObject.toString());
        }
    }

    @Override
    public void keepAlive() {
        logger.info("enter douyu keep alive");


        this.push(String.format("type@=keeplive/tick@=%s/", String.valueOf(System.currentTimeMillis())));
        ThreadUtil.safeSleep(30000);
    }
}
