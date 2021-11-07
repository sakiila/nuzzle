package me.bob.nuzzle.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import me.bob.nuzzle.data.RpcResponse;

@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            RpcResponse response = (RpcResponse) msg;
            log.info("client receive message:{}", response);

            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            ctx.channel().attr(key).set(response);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client exception", cause);
        ctx.close();
    }
}
