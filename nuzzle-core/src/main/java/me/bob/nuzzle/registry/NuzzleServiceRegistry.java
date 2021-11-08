package me.bob.nuzzle.registry;

import java.net.InetSocketAddress;

public class NuzzleServiceRegistry {

    public static void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String path = rpcServiceName + inetSocketAddress;
        CuratorUtils.createNode(path);
    }
}
