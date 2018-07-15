package com.hunter.hrpc.service.impl;

import com.hunter.hrpc.annotations.RpcService;
import com.hunter.hrpc.service.HelloRpcService;

/**
 * @version 1.0.0
 * @author: hunter
 * @date: 2018/7/15 23:07
 * @description:
 */
// 指定远程接口
@RpcService(RpcService.class)
public class HelloRpcServiceImpl implements HelloRpcService {

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }
}

