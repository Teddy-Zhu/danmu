package com.silentgo.danmu.client.douyu;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.silentgo.danmu.base.BaseMsg;
import com.silentgo.danmu.base.DanMuClient;
import com.silentgo.danmu.client.douyu.model.DouyuBaseMsg;
import com.silentgo.danmu.client.douyu.model.msg.ChatMsg;
import com.silentgo.danmu.client.douyu.model.msg.DgbMsg;
import com.silentgo.danmu.client.douyu.model.msg.SpbcMsg;
import com.silentgo.danmu.client.douyu.model.msg.UenterMsg;
import com.silentgo.danmu.netty.NettyClient;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DouyuClient implements DanMuClient {

    public static final Logger logger = LoggerFactory.getLogger(DouyuClient.class);

    private String roomId;

    private NettyClient nettyClient;

    //斗鱼官方心跳要求45
    private static final long heartBeatInterval = 40 * 1000;

    public DouyuClient(String url) {
        String mark = url.split("/")[3];

        String roomUrl = String.format("http://open.douyucdn.cn/api/RoomApi/room/%s", mark);
        JSONObject json = JSONUtil.parseObj(HttpUtil.get(roomUrl));
        if (json.getInt("error") != 0 || !"1".equals(json.getJSONObject("data").getStr("room_status"))) {

            return;
        }

        roomId = json.getJSONObject("data").getStr("room_id");
        logger.info("get room id:{}", roomId);

    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public void init() {
        nettyClient = new NettyClient("openbarrage.douyutv.com", 8601);

        List<ChannelHandler> channelHandlerList = nettyClient.getChannelHandlers();
        channelHandlerList.add(new DouyuMsgDecoder());
        channelHandlerList.add(new DouyuMsgEncoder());
        channelHandlerList.add(new DouyuSimpleHandler(this));
//        channelHandlerList.add(new IdleStateHandler(0, 20, 0, TimeUnit.SECONDS));
//        channelHandlerList.add(new DouyuIdleStateHandler());


    }

    @Override
    public void start() {
        nettyClient.connect(new Runnable() {
            @Override
            public void run() {
                while (nettyClient.isAlive()) {
                    logger.info("send douyu heart beat msg");
                    String hearttext = "type@=mrkl/";
                    nettyClient.write(hearttext);
                    ThreadUtil.safeSleep(heartBeatInterval);
                }
            }
        });
    }

    @Override
    public void resolveMsg(BaseMsg msg) {
        DouyuBaseMsg douyuBaseMsg = (DouyuBaseMsg) msg;
        String type = douyuBaseMsg.getType();
        switch (type) {
            case "chatmsg":
                logger.info(new ChatMsg(douyuBaseMsg).toString());
                break;
            case "dgb":
                logger.info(new DgbMsg(douyuBaseMsg).toString());
                break;
            case "uenter":
                logger.info(new UenterMsg(douyuBaseMsg).toString());
                break;
            case "spbc":
                logger.info(new SpbcMsg(douyuBaseMsg).toString());
                break;
            default:
                logger.info("othertype:" + douyuBaseMsg.getContent());
                break;
        }
    }
}
