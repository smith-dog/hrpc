package com.hunter.hrpc.registry;

import com.hunter.hrpc.properties.RpcProperties;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @param:
 * @return:
 * @author: hunter
 * @date: 2018/7/15 23:04
 */
public class ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    @Autowired
    private RpcProperties hRpcProperties;

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void register(String data) {
        if (data != null) {
            ZooKeeper zk = connectServer();
            if (zk != null) {
                createNode(zk, data);
            }
        }
    }



    /**
     * 创建会话
     * @return
     */
    private ZooKeeper connectServer() {

        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, hRpcProperties.getZkSessionTimeout(), new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zk;
    }

    /**
     * 创建节点
     * @param zk
     * @param data
     */
    private void createNode(ZooKeeper zk, String data) {
        createNode(zk, data, hRpcProperties.getZkDataPath());
    }

    /**
     * 创建节点
     * @param zk
     * @param data
     */
    private void createNode(ZooKeeper zk, String data, String zkDataPath) {
        try {
            byte[] bytes = data.getBytes();
            //临时顺序节点
            String path = zk.create(zkDataPath, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("create zookeeper node ({} => {})", path, data);
        } catch (KeeperException | InterruptedException e) {
            logger.error("", e);
        }
    }
}

