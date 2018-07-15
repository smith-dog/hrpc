package com.hunter.hrpc.service;

import com.hunter.hrpc.proxy.RpcProxy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @version 1.0.0
 * @author: hunter
 * @date: 2018/7/16 00:50
 * @description:
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class HelloRpcServiceTest {
    public class HelloServiceTest {

        @Autowired
        private RpcProxy rpcProxy;

        @Test
        public void helloTest() {
            HelloRpcService helloService = rpcProxy.create(HelloRpcService.class);
            String result = helloService.hello("World");
            Assert.assertEquals("Hello! World", result);
        }
    }
}