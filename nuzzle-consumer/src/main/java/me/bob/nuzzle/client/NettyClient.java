package me.bob.nuzzle.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import me.bob.nuzzle.data.RpcRequest;
import me.bob.nuzzle.data.RpcResponse;
import me.bob.nuzzle.handler.NettyClientHandler;
import me.bob.nuzzle.registry.CuratorUtils;
import me.bob.nuzzle.registry.NuzzleServiceRegistry;
import me.bob.nuzzle.serializer.KryoSerializer;
import me.bob.nuzzle.serializer.NettyDecode;
import me.bob.nuzzle.serializer.NettyEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@Service
@Slf4j
public class NettyClient {

    public static Bootstrap bootstrap;

    @PostConstruct
    public void init() {
        register();
        
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        final KryoSerializer kryoSerializer = new KryoSerializer();

        try {
            bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyDecode(kryoSerializer, RpcResponse.class));
                            socketChannel.pipeline().addLast(new NettyEncoder(kryoSerializer, RpcRequest.class));
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });

//            f = bootstrap.connect("127.0.0.1", 8099).sync();
//            f.channel().closeFuture().sync();
        } finally {
//            workerGroup.shutdownGracefully();
        }
    }

    public void register(){
        // 使用注册中心
        CuratorUtils.initZkClient();
    }
}
