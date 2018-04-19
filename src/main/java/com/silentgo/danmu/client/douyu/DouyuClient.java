package com.silentgo.danmu.client.douyu;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.silentgo.danmu.base.DanMuClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class DouyuClient extends DanMuClient {

    public static final Logger logger = LoggerFactory.getLogger(DouyuClient.class);

    private String roomId;

    public DouyuClient(String url) {
        super(url);
    }

    @Override
    public boolean getLiveStatus() {

        //room mark
        String mark = getUrl().split("/")[3];

        String url = String.format("http://open.douyucdn.cn/api/RoomApi/room/%s", mark);
        JSONObject json = JSONUtil.parseObj(HttpUtil.get(url));
        if (json.getInt("error") != 0 || !"1".equals(json.getJSONObject("data").getStr("room_status"))) {
            return false;
        }

        roomId = json.getJSONObject("data").getStr("room_id");

        return true;
    }

    @Override
    public void initSocket() {
        try {
            Socket socket = new Socket("openbarrage.douyutv.com", 8601);
            this.setDanMuSocket(new DouyuDanMuSocket(socket));
            this.getDanMuSocket().push(String.format("type@=loginreq/roomid@=%s/", roomId));
            this.getDanMuSocket().push(String.format("type@=joingroup/rid@=%s/gid@=-9999/", roomId));
        } catch (IOException e) {
            logger.error("create socket for douyu error", e);
        }
    }

    @Override
    public void prepareEnv() {

    }

    @Override
    public void createThreadFn() {

        Thread danMuThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    logger.info("pull danmu : {}", getDanMuSocket().getDanmu());
//                    if (getDanmuWaitTime() != -1 && getDanmuWaitTime() < System.currentTimeMillis()) {
//                        logger.warn("No danmu received in {}", getDanmuWaitTime());
//                        break;
//                    } else {
//                        logger.info("pull danmu : {}", getDanMuSocket().getDanmu());
//                        setDanmuWaitTime(System.currentTimeMillis() + getMaxNoDanMuWait());
//                    }
                }

            }
        });
        danMuThread.setDaemon(true);
        this.setDanmuThread(danMuThread);

        Thread heartThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isLive() && !isDeprecated()) {
                    getDanMuSocket().keepAlive();
                }
            }
        });
        heartThread.setDaemon(true);

        this.setHeartThread(heartThread);
    }


    @Override
    public void startService() {
        setLive(true);
        getDanmuThread().start();
       // getHeartThread().start();
        setDanmuWaitTime(System.currentTimeMillis() + 20);
    }


}
