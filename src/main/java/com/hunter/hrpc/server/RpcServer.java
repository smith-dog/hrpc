package com.hunter.hrpc.server;

import com.hunter.hrpc.annotations.RpcService;
import com.hunter.hrpc.coder.RpcDecoder;
import com.hunter.hrpc.coder.RpcEncoder;
import com.hunter.hrpc.entity.RpcRequest;
import com.hunter.hrpc.entity.RpcResponse;
import com.hunter.hrpc.handler.RpcHandler;
import com.hunter.hrpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @param:
 * @return:
 * @author: hunter
 * @date: 2018/7/15 23:03
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

    private String serverAddress;

    private ServiceRegistry serviceRegistry;

    // 存放接口名与服务对象之间的映射关系
    private Map<String, Object> handlerMap = new HashMap<>();

    private final static Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public RpcServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcServer(String serverAddress, ServiceRegistry serviceRegistry) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取所有带有 RpcService 注解的 Spring Bean
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (serviceBeanMap != null && !serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(interfaceName, serviceBean);
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    // 将 RPC 请求进行解码（为了处理请求
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    // 将 RPC 响应进行编码（为了返回响应）
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    // 处理 RPC 请求
                                    .addLast(new RpcHandler(handlerMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.debug("server started on port {}", port);

            if (serviceRegistry != null) {
                // 注册服务地址
                serviceRegistry.register(serverAddress);
            }

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
