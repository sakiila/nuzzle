package me.bob.nuzzle.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import me.bob.nuzzle.data.RpcRequest;
import me.bob.nuzzle.data.RpcResponse;
import me.bob.nuzzle.handler.NettyServerHandler;
import me.bob.nuzzle.registry.CuratorUtils;
import me.bob.nuzzle.registry.NuzzleServiceRegistry;
import me.bob.nuzzle.serializer.KryoSerializer;
import me.bob.nuzzle.serializer.NettyDecode;
import me.bob.nuzzle.serializer.NettyEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Service
@Slf4j
public class NettyServer {

    @PostConstruct
    public void init() {
        register();
        
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final KryoSerializer kryoSerializer = new KryoSerializer();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyDecode(kryoSerializer, RpcRequest.class));
                            socketChannel.pipeline().addLast(new NettyEncoder(kryoSerializer, RpcResponse.class));
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


            final ChannelFuture f = serverBootstrap.bind(8099).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
    
    public void register(){
        // 使用注册中心注册服务
        CuratorUtils.initZkClient();
        CuratorUtils.deleteAll();
        InetSocketAddress inetSocketAddress;
        try {
            inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 8099);
            log.info("inetSocketAddress {}", inetSocketAddress);
            NuzzleServiceRegistry.registerService("UserServiceImpl", inetSocketAddress);
        } catch (UnknownHostException e) {
            log.error("InetAddress.getLocalHost() exception", e);
        }
    }
}
