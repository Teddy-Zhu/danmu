package com.silentgo.danmu.client.douyu;

import com.silentgo.danmu.base.DanMuClient;
import com.silentgo.danmu.netty.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DouyuClient extends DanMuClient {

    public static final Logger logger = LoggerFactory.getLogger(DouyuClient.class);

    private String roomId;

    public static final char[] douyuReceiveMark = new char[]{0xb1, 0x02, 0x00, 0x00};

    public static final char[] douyuSendMark = new char[]{0xb1, 0x02, 0x00, 0x00};


    public DouyuClient(String url) {
        super(url);
    }

    @Override
    public boolean getLiveStatus() {

        logger.info("enter douyu live status");
        //room mark
        /*
        String mark = getUrl().split("/")[3];

        String url = String.format("http://open.douyucdn.cn/api/RoomApi/room/%s", mark);
        JSONObject json = JSONUtil.parseObj(HttpUtil.get(url));
        if (json.getInt("error") != 0 || !"1".equals(json.getJSONObject("data").getStr("room_status"))) {
            return false;
        }

        roomId = json.getJSONObject("data").getStr("room_id");
        */
        roomId = "4809";
        logger.info("get room id:{}", roomId);

        return true;
    }

    @Override
    public void initSocket() {
        NettyClient nettyClient = new NettyClient("openbarrage.douyutv.com", 8601, douyuReceiveMark);
        nettyClient.connect();
        this.setDanMuSocket(new DouyuDanMuSocket(nettyClient));
        this.getDanMuSocket().push(String.format("type@=loginreq/roomid@=%s/", roomId));
        this.getDanMuSocket().push(String.format("type@=joingroup/rid@=%s/gid@=-9999/", roomId));
    }

    @Override
    public void prepareEnv() {

    }

    @Override
    public void createThreadFn() {

        Thread heartThread = new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("enter danmu client heart thread");

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
        logger.info("enter douyu client start service");
        setLive(true);
        getHeartThread().start();
        setDanmuWaitTime(System.currentTimeMillis() + 20);
    }


}
