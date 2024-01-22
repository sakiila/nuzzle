<p align="center" >
    <a href="https://github.com/sakiila/nuzzle"><img src="https://raw.githubusercontent.com/sakiila/nuzzle/main/nuzzle.png" width="50%"></a>
</p>

# nuzzle

A Simple RPC Project.

一个简洁的 RPC 项目，用于学习与研究。

使用到的技术：Socket、Netty、CGlib、Kryo、ZooKeeper 等。Api 包提供服务接口；Core 包提供服务注册与发现，Socket 与 Netty 连接方式，Kyro 序列化等功能；Consumer 包使用 CGlib 动态代理，从 ZooKeeper 获取服务地址，通过 Netty 连接服务端；Provider 包使用 ZooKeeper 注册服务与地址信息，使用动态代理调用实际的服务。

### nuzzle-core

框架核心部分

### nuzzle-api

接口定义部分

### nuzzle-consumer

客户端

### nuzzle-provider
服务端
