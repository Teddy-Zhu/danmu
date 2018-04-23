package com.silentgo.danmu.client.douyu;

import com.silentgo.danmu.client.douyu.model.DouyuBaseMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DouyuSimpleHandler extends SimpleChannelInboundHandler<String> {

    public static final Logger logger = LoggerFactory.getLogger(DouyuSimpleHandler.class);

    private DouyuClient douyuClient;

    public DouyuSimpleHandler(DouyuClient douyuClient) {
        this.douyuClient = douyuClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        DouyuBaseMsg douyuBaseMsg = new DouyuBaseMsg(s);

        String type = douyuBaseMsg.getType();
        switch (type) {
            case "loginres":
                //加入群组海量弹幕
                String joingrop = String.format("type@=joingroup/rid@=%s/gid@=-9999/", douyuClient.getRoomId());
                channelHandlerContext.writeAndFlush(joingrop);
                break;
            case "qausrespond":

                logger.info("send douyu heart beat msg");
                String hearttext = "type@=mrkl/";
                channelHandlerContext.writeAndFlush(hearttext);
                break;
            default:
                douyuClient.resolveMsg(douyuBaseMsg);
                break;

        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("active , start enter room");
        String logininfo = String.format("type@=loginreq/roomid@=%s/", douyuClient.getRoomId());
        ctx.writeAndFlush(logininfo);
    }

}
