package com.silentgo.danmu.client.panda;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class PandaMsgDecoder extends ByteToMessageDecoder {
    public static final byte[] Common_Header = new byte[]{0x00, 0x06, 0x00, 0x03};
    public static final byte[] Data_Header = new byte[]{0x00, 0x06, 0x00, 0x06};
    public static final Logger logger = LoggerFactory.getLogger(PandaMsgDecoder.class);


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //收到数据之后解析熊猫消息
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();

        byte[] mark = new byte[4];
        byteBuf.readBytes(mark);


        if (Arrays.equals(Common_Header, mark)) {

            // 4+7+4 header skip datalength
            if (byteBuf.readableBytes() < (7 + 4)) {
                byteBuf.resetReaderIndex();
                return;
            }

            //skip 7 byte
            byteBuf.skipBytes(7);


            byte[] lengthBytes = new byte[4];
            byteBuf.readBytes(lengthBytes);
            //读取4个字节，得到数据长度

            int contentLen = ntohl(lengthBytes);


            logger.info("read bytes:{}, len :{}", lengthBytes, contentLen);


            if (byteBuf.readableBytes() < contentLen) {
                byteBuf.resetReaderIndex();
                logger.info("reset try read contentLen again");
                return;
            }

            byte[] data = new byte[contentLen];

            byteBuf.readBytes(data);

            //弹幕 8-12 为固定标志
            byte[] b = Arrays.copyOfRange(data, 8, 12);

            for (int i = 0; i < data.length; ) {
                //一段弹幕内容的开头
                if (data[i] == b[0] && data[i + 1] == b[1] && data[i + 2] == b[2] && data[i + 3] == b[3]) {
                    i += 4;
                    //一段弹幕json字符串的长度
                    int length = 0;
                    //读取4个字节，得到弹幕数据长度
                    for (int j = 0, k = 3; j < 4; j++, k--) {
                        int n = data[i + j];
                        /*
                          原数据一个字节可保存0~255的数,但是byte范围是-128~127,所以要变回原来的真实数据
                          后面的数据不变是因为后面的字符串都是ascii字符,都在0~127之内
                         */
                        if (n < 0) {
                            n = 256 + data[i + j];
                        }

                        length += n * Math.pow(16, 2 * k);
                    }
                    i += 4;

                    list.add(new String(Arrays.copyOfRange(data, i, i + length), "UTF-8"));

                    i += length;
                } else {
                    i++;
                }
            }

        } else if (Arrays.equals(Data_Header, mark)) {

            byte[] lengthBytes = new byte[2];
            byteBuf.readBytes(lengthBytes);

            //下条内容的长度
            int contentLen = ntohs(lengthBytes);

            logger.info("skip data length :{}", contentLen);
            byteBuf.skipBytes(contentLen);

        }


    }

    public static int ntohl(byte[] bys) {
        return ByteBuffer.wrap(bys).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public static int ntohs(byte[] bys) {
        return ByteBuffer.wrap(bys).order(ByteOrder.BIG_ENDIAN).getShort();
    }


}
