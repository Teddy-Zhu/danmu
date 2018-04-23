package com.silentgo.danmu.client.panda;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.silentgo.danmu.base.BaseMsg;
import com.silentgo.danmu.base.DanMuClient;
import com.silentgo.danmu.exception.BusinessException;
import com.silentgo.danmu.netty.NettyClient;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class PandaClient implements DanMuClient {


    public static final Logger logger = LoggerFactory.getLogger(PandaClient.class);

    private String roomId;

    private JSONObject serverInfo;

    private NettyClient nettyClient;

    private static final long heartBeatInterval = 60 * 1000;

    public PandaClient(String url) {
        //https://www.panda.tv/116304
        Map<String, Object> params = MapUtil.newHashMap();
        roomId = url.split("/")[3];
        params.put("roomid", roomId);
        params.put("pub_key", "");
        params.put("_", System.currentTimeMillis());

        JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.get("http://www.panda.tv/api_room", params));

        if (jsonObject.getJSONObject("data").getJSONObject("videoinfo").getInt("status") != 2) {
            throw new BusinessException("room is close");
        }
    }

    @Override
    public void init() {

        Map<String, Object> params = MapUtil.newHashMap();
        params.put("roomid", roomId);
        params.put("app", 1);
        params.put("_caller", "panda-pc_web");
        params.put("_", System.currentTimeMillis());

        serverInfo = JSONUtil.parseObj(HttpUtil.get("https://riven.panda.tv/chatroom/getinfo", params)).getJSONObject("data");

        String[] serverUrl = serverInfo.getJSONArray("chat_addr_list").get(0).toString().split(":");

        logger.info("login server :{}", serverUrl);
        nettyClient = new NettyClient(serverUrl[0], Integer.valueOf(serverUrl[1]));

        List<ChannelHandler> channelHandlerList = nettyClient.getChannelHandlers();
        channelHandlerList.add(new PandaMsgEncoder());
        channelHandlerList.add(new PandaMsgDecoder());
        channelHandlerList.add(new PandaSimpleHandler(this));

    }

    @Override
    public void start() {

        nettyClient.connect(new Runnable() {
            @Override
            public void run() {
                while (nettyClient.isAlive()) {
                    logger.info("send panda heart beat msg");
                    nettyClient.write(new byte[]{0x00, 0x06, 0x00, 0x00});
                    ThreadUtil.safeSleep(heartBeatInterval);
                }
            }
        });
    }

    @Override
    public void resolveMsg(BaseMsg msg) {

    }


    public JSONObject getServerInfo() {
        return serverInfo;
    }
}
