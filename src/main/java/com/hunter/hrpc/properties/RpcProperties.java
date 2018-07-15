package com.hunter.hrpc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @param: 
 * @return: 
 * @author: hunter
 * @date: 2018/7/15 23:05
 */
@Component
@Data
@ConfigurationProperties(prefix = "Rpc")
public class RpcProperties {
    private int zkSessionTimeout;

    private String zkDataPath;

    private String zkRegistryPath;

    private String registryAddress;

    private String serverAddress;
}
