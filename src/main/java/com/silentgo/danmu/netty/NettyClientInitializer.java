package com.silentgo.danmu.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.List;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    List<ChannelHandler> channelHandlers;


    public NettyClientInitializer(NettyClient nettyClient) {
        this.channelHandlers = nettyClient.getChannelHandlers();
    }

    @Override
    public void initChannel(SocketChannel ch) {

        ChannelPipeline pipeline = ch.pipeline();
        for (ChannelHandler channelHandler : channelHandlers) {
            pipeline.addLast(channelHandler);
        }
    }
}
