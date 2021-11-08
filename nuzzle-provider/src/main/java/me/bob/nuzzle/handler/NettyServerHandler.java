package me.bob.nuzzle.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import me.bob.nuzzle.data.RpcRequest;
import me.bob.nuzzle.data.RpcResponse;
import me.bob.nuzzle.impl.UserServiceImpl;
import me.bob.nuzzle.registry.NuzzleServiceRegistry;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            RpcRequest<UserServiceImpl> request = (RpcRequest<UserServiceImpl>) msg;
            log.info("server receive msg: {}", request);

            // 动态代理，调用实际的UserServiceImpl方法
            final Class<UserServiceImpl> serviceClass = request.getServiceClass();
            final String methodName = request.getMethodName();
            final Object[] params = request.getParams();

            final UserServiceImpl userService = serviceClass.newInstance();
            final Method method = serviceClass.getDeclaredMethod(methodName, String.class);
            final Object invoke = method.invoke(userService, params);
            log.error("invoke: {}", invoke.toString());

            final RpcResponse response = new RpcResponse().setResult(invoke).setStatus(true);
            final ChannelFuture channelFuture = ctx.writeAndFlush(response);
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server exception", cause);
        ctx.close();
    }
}
