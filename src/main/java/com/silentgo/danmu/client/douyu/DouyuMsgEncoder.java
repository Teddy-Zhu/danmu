package com.silentgo.danmu.client.douyu;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DouyuMsgEncoder extends MessageToByteEncoder<String> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        //发送时封装斗鱼协议
        //(所有协议内容均为 UTF-8 编码)
        //689 客户端发送给弹幕服务器的文本格式数据
        //690 弹幕服务器发送给客户端的文本格式数据。
        byte[] bytes = s.getBytes("UTF-8");
        int framelen = bytes.length + 4 + 4 + 1;
        byteBuf.writeBytes(htonl(framelen));
        byteBuf.writeBytes(htonl(framelen));
        byteBuf.writeBytes(htons((short) 689));
        byteBuf.writeBytes(htons((short) 0));

        //斗鱼协议头结束
        //写数据
        byteBuf.writeBytes(bytes);
        byteBuf.writeByte(0);

    }


    public static byte[] htonl(int i) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array();
    }

    public static byte[] htons(short i) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(i).array();
    }

}
