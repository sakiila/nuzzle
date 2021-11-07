package me.bob.nuzzle.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@AllArgsConstructor
@Slf4j
public class NettyDecode extends ByteToMessageDecoder {
    
    private Serializer serializer;

    private Class<?> genericClass;
    
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 4) {
            byteBuf.markReaderIndex();
            
            final int length = byteBuf.readInt();
            if (length < 0 || byteBuf.readableBytes() < 0) {
                log.error("data length is not valid");
                return;
            }

            if (byteBuf.readableBytes() < 4) {
                byteBuf.resetReaderIndex();
                return;
            }

            final byte[] body = new byte[length];
            byteBuf.readBytes(body);

            final Object o = serializer.deserialize(body, genericClass);
            list.add(o);
            log.info("successful decode");
        }
    }
}
