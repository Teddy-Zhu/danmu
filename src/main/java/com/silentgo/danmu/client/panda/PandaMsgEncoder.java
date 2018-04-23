package com.silentgo.danmu.client.panda;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class PandaMsgEncoder extends MessageToByteEncoder<String> {

    public static final int[] header = new int[]{0x00, 0x06, 0x00, 0x02};

    public static final int[] data_end = new int[]{0x00, 0x06, 0x00, 0x00};

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        //发送时封装熊猫的协议
        // header \x00\x06\x00\x02\x00
        // heart beat \x00\x06\x00\x00

        byte[] bytes = s.getBytes("UTF-8");

        if (bytes.length == 4 && bytes[0] == 0x00 && bytes[1] == 0x06
                && bytes[2] == 0x00 && bytes[3] == 0x00) {
            //心跳包
            byteBuf.writeBytes(bytes);

        } else {
            for (int i : PandaMsgEncoder.header) {
                byteBuf.writeByte(i);
            }

            byteBuf.writeBytes(htons((short) bytes.length));

            byteBuf.writeBytes(bytes);

            for (int i : PandaMsgEncoder.data_end) {
                byteBuf.writeByte(i);
            }
        }


    }

    public static byte[] htons(short i) {
        return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(i).array();
    }

}
