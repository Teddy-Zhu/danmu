package com.silentgo.danmu.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;
    private String charset = "utf-8";
    private ChannelFuture future;
    private EventLoopGroup workerGroup;

    private List<ChannelHandler> channelHandlers = new ArrayList<>();

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        logger.info("enter netty connect");

        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.handler(new NettyClientInitializer(this));
            future = bootstrap.connect(host, port).sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            workerGroup.shutdownGracefully();
        }
    }


    public String getCharset() {
        return charset;
    }


    public boolean isAlive() {
        return future.channel().isActive();
    }

    public void close() {
        try {
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


    public void write(byte[] bytes) {
        logger.info("enter netty write byte");

        future.channel().writeAndFlush(bytes);
    }

    public void write(String text) {
        logger.info("enter netty write text");

        future = future.channel().writeAndFlush(text);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    public List<ChannelHandler> getChannelHandlers() {
        return channelHandlers;
    }
}
