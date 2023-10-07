<p align="center" >
    <a href="https://github.com/sakiila/nuzzle"><img src="https://raw.githubusercontent.com/sakiila/nuzzle/main/nuzzle.png" width="50%"></a>
</p>

# nuzzle

This RPC Project is a concise and purposeful undertaking aimed at facilitating learning and research in the field. It incorporates a range of technologies such as Socket, Netty, CGlib, Kryo, and ZooKeeper.

当前 RPC 项目是一项简洁且有目的的项目，旨在促进该领域的学习和研究。使用了一系列技术，例如 Socket、Netty、CGlib、Kryo 和 ZooKeeper。

### nuzzle-core
框架核心部分，提供服务注册与发现、Socket 与 Netty 连接方式、Kyro 序列化等功能。

### nuzzle-api
接口定义部分，提供服务接口。

### nuzzle-consumer
客户端，使用 CGlib 动态代理，从 ZooKeeper 获取服务地址，通过 Netty 连接服务端。

### nuzzle-provider
服务端，使用 ZooKeeper 注册服务与地址信息，使用动态代理调用实际的服务。

# 使用教程

### Socket 方式
refer: [nuzzle-provider/src/main/java/me/bob/nuzzle/server/SocketServer.java](nuzzle-provider/src/main/java/me/bob/nuzzle/server/SocketServer.java)

### Netty 方式
refer: [nuzzle-provider/src/main/java/me/bob/nuzzle/server/NettyServer.java](nuzzle-provider/src/main/java/me/bob/nuzzle/server/NettyServer.java)