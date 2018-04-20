package com.silentgo.danmu.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    private NettyClient nettyClient;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        logger.info("receive msg :{}", msg);
        if (nettyClient.getMsgResolver() != null) {
            nettyClient.getMsgResolver().resolveMsg(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("read exception", cause);
        ctx.close();
    }

    public NettyClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }
}
