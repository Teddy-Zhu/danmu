package com.silentgo.danmu.client.douyu;

import com.silentgo.danmu.exception.BusinessException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class DouyuMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //收到数据之后解析斗鱼消息
        if (byteBuf.readableBytes() < 12) {
            return;
        }
        byteBuf.markReaderIndex();
        byte[] bys = new byte[4];
        byteBuf.readBytes(bys);
        int size = ntohl(bys);
        // 等到读完一个整个体消息
        int readablebytes = byteBuf.readableBytes();
        if (readablebytes < size) {
            //重置
            byteBuf.resetReaderIndex();
            return;
        }
        byteBuf.readBytes(bys);
        int size2 = ntohl(bys);

        //根据文档 两个size 应该相同

        //消息类型
        short msgType = byteBuf.readShort();
        //保留字段
        short keepfield = byteBuf.readShort();

        if (size != size2) {
            byteBuf.resetReaderIndex();
            throw new BusinessException("data head valid size error");
        }

        //数据结尾'\0'

        byte[] decode = new byte[size - 9];
        byteBuf.readBytes(decode);
        byte dataEnd = byteBuf.readByte();
        if (dataEnd != 0) {
            byteBuf.resetReaderIndex();
            throw new Exception("data end mark error");
        }
        list.add(new String(decode, "UTF-8"));
    }

    public static int ntohl(byte[] bys) {
        return ByteBuffer.wrap(bys).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

}
