package com.silentgo.danmu.client.panda;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.silentgo.danmu.client.panda.model.PandaBaseMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PandaSimpleHandler extends SimpleChannelInboundHandler<String> {

    public static final Logger logger = LoggerFactory.getLogger(PandaSimpleHandler.class);

    private PandaClient pandaClient;

    public PandaSimpleHandler(PandaClient pandaClient) {
        this.pandaClient = pandaClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        PandaBaseMsg pandaBaseMsg = new PandaBaseMsg(s);


        logger.info("receive :{}", s);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("active , start enter panda room");

        JSONObject serverInfo = pandaClient.getServerInfo();

        Map<String, Object> params = MapUtil.newHashMap();
        params.put("u", String.format("%s@%s", serverInfo.getStr("rid"), serverInfo.getStr("appid")));
        params.put("k", 1);
        params.put("t", 300);
        params.put("ts", serverInfo.getStr("ts"));
        params.put("sign", serverInfo.getStr("sign"));
        params.put("authtype", serverInfo.getStr("authType"));

        String data = MapUtil.join(params, "\n", ":");

        ctx.writeAndFlush(data);

    }

}
