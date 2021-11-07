package me.bob.nuzzle.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Slf4j
public class NettyEncoder extends MessageToByteEncoder<Object> {
    
    private Serializer serializer;
    
    private Class<?> genericClass;
    
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(o)) {
            final byte[] body = serializer.serialize(o);
            final int length = body.length;
            
            byteBuf.writeInt(length);
            byteBuf.writeBytes(body);
            log.info("successful encode");
        }
    }
}
