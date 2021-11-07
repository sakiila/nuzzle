package me.bob.nuzzle.controller;

import lombok.extern.slf4j.Slf4j;
import me.bob.nuzzle.api.UserService;
import me.bob.nuzzle.client.NettyClient;
import me.bob.nuzzle.data.RpcResponse;
import me.bob.nuzzle.impl.UserServiceImpl;
import me.bob.nuzzle.proxy.NuzzleProxyFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@RestController
@Slf4j
public class ConsumerController {

    @RequestMapping("/hello")
    public String sayHi(String name) {
        try (Socket socket = new Socket("127.0.0.1", 8090)) {

            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            final String request = "Hello!";
            objectOutputStream.writeObject(request);

            final ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            final String messaage = (String) objectInputStream.readObject();
            return messaage;
        } catch (IOException | ClassNotFoundException e) {
            log.error("Exception", e);
        }
        return "Good";
    }
    
//    @RequestMapping("/hello2")
//    public RpcResponse sayHi2(String name) throws InterruptedException {
//
//        final Bootstrap bootstrap = NettyClient.bootstrap;
//        final ChannelFuture f = bootstrap.connect("127.0.0.1", 8099).sync();
//        Channel channel = f.channel();
//        log.info("send message starting");
//        
//        if (channel != null) {
//            final RpcRequest rpcRequest = new RpcRequest();
//            rpcRequest.setMethodName("method");
//            channel.writeAndFlush(rpcRequest)
//                    .addListener(future -> {
//                        if (future.isSuccess()) {
//                            log.info("client send message");
//                        } else {
//                            log.error("send failed", future.cause());
//                        }
//                    });
//
//            channel.closeFuture().sync();
//
//            final AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
//            return channel.attr(key).get();
//        }
//
//        return null;
//    }

    @RequestMapping("/hello3")
    public String sayHi3(String name) throws InterruptedException {

        UserService userService = (UserService) NuzzleProxyFactory.getProxy(UserServiceImpl.class, NettyClient.bootstrap);

        return userService.sayHi("hello3");
    }

}
