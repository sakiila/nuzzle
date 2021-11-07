package me.bob.nuzzle.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bob.nuzzle.data.RpcRequest;
import me.bob.nuzzle.data.RpcResponse;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@AllArgsConstructor
@Slf4j
public class NuzzleInterceptor implements MethodInterceptor {

    private Class clazz;

    private Bootstrap bootstrap;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        log.info("start invoke " + method.getName());

        final ChannelFuture f = bootstrap.connect("127.0.0.1", 8099).sync();
        Channel channel = f.channel();
        log.info("send message starting");

        if (channel != null) {
            final RpcRequest rpcRequest = new RpcRequest<>();
            rpcRequest.setServiceClass(clazz)
                    .setMethodName(method.getName())
                    .setParams(objects);
            channel.writeAndFlush(rpcRequest)
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            log.info("send succeed");
                        } else {
                            log.error("send failed", future.cause());
                        }
                    });

            channel.closeFuture().sync();
        }

        final AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
        final RpcResponse response = channel.attr(key).get();

        return response.getResult();
    }
}
