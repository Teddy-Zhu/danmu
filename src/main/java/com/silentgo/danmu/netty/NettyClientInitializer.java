package com.silentgo.danmu.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private StringDecoder DECODER;
    private StringEncoder ENCODER;

    private ByteBuf byteBuf;

    private NettyClientHandler CLIENT_HANDLER;

    public NettyClientInitializer(NettyClient nettyClient) {
        this.DECODER = new StringDecoder(Charset.forName(nettyClient.getCharset()));
        this.ENCODER = new StringEncoder(Charset.forName(nettyClient.getCharset()));
        this.CLIENT_HANDLER = new NettyClientHandler(nettyClient);
        this.byteBuf = nettyClient.getDelimiter();
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new DelimiterBasedFrameDecoder(1000, this.byteBuf));
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);
        pipeline.addLast(CLIENT_HANDLER);
    }
}
