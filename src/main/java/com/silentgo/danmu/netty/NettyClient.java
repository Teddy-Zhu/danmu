package com.silentgo.danmu.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;


public class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;
    private String charset = "utf-8";
    private ChannelFuture future;
    private ByteBuf delimiter;

    private EventLoopGroup workerGroup;

    private NettyMsgResolver msgResolver;

    public NettyClient(String host, int port, byte[] delimiter) {
        this.host = host;
        this.port = port;
        this.delimiter = Unpooled.copiedBuffer(delimiter);
    }

    public NettyClient(String host, int port, char[] delimiter) {
        this.host = host;
        this.port = port;
        this.delimiter = Unpooled.copiedBuffer(delimiter, Charset.forName(charset));
    }

    public NettyClient(String host, int port, byte[] delimiter, NettyMsgResolver msgResolver) {
        this.host = host;
        this.port = port;
        this.delimiter = Unpooled.copiedBuffer(delimiter);
        this.msgResolver = msgResolver;
    }

    public void connect() {
        logger.info("enter netty connect");

        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, false);
            bootstrap.handler(new NettyClientInitializer(this));
            future = bootstrap.connect(host, port).sync();

        } catch (Exception e) {
            workerGroup.shutdownGracefully();
        }
    }


    public String getCharset() {
        return charset;
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

    public ByteBuf getDelimiter() {
        return delimiter;
    }

    public NettyMsgResolver getMsgResolver() {
        return msgResolver;
    }

    public void setMsgResolver(NettyMsgResolver msgResolver) {
        this.msgResolver = msgResolver;
    }
}
