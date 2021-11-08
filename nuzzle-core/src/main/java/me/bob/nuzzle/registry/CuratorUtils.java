package me.bob.nuzzle.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CuratorUtils {

    public static final String ROOT_PATH = "/sakila";
    
    private static CuratorFramework zkClient;

    public static CuratorFramework initZkClient() {
        if (zkClient != null) {
            return zkClient;
        }

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        zkClient = CuratorFrameworkFactory.builder()
                .retryPolicy(retryPolicy)
                .connectString("127.0.0.1:2181").build();
        zkClient.start();
        
        return zkClient;
    }

    public static void createNode(String path) {
        path = ROOT_PATH + "/" + path;
        try {
            if (zkClient.checkExists().forPath(path) != null) {
                log.info("The node already exists. The node is:{}", path);
            } else {
                zkClient.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(path);
                log.info("The node has created. The node is:{}", path);
            }
        } catch (Exception e) {
            log.error("createNode exception", e);
        }
    }

    public static List<String> getNode(String rpcServerName) {
        List<String> results = new ArrayList<>();
        final String path = ROOT_PATH + "/" + rpcServerName;
        try {
            results = zkClient.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("getNode exception", e);
        }

        try (PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, path, true)) {
            PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
                final List<String> strings = curatorFramework.getChildren().forPath(path);
                // TODO: 2021/11/8 local cache 
            };
            pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
            pathChildrenCache.start();
        } catch (Exception e) {
            log.error("watch exception", e);
        }

        return results;
    }

    public static void deleteAll() {
        try {
            if (zkClient.checkExists().forPath(ROOT_PATH) != null) {
                zkClient.delete().deletingChildrenIfNeeded().forPath(ROOT_PATH);
            }
        } catch (Exception e) {
            log.error("delete node exception", e);
        }
    }
}
