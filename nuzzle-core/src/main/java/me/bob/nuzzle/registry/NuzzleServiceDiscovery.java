package me.bob.nuzzle.registry;

import java.net.InetSocketAddress;
import java.util.List;

public class NuzzleServiceDiscovery {

    public static InetSocketAddress lookupService(String serviceName) {
        final List<String> node = CuratorUtils.getNode(serviceName);
        // TODO: 2021/11/8 load-balance
        String address = node.get(0);
        final String[] split = address.split(":");
        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    }
}
