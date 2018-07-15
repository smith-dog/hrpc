package com.hunter.hrpc.entity;

import lombok.Data;

/**
 * @version 1.0.0
 * @author: hunter
 * @date: 2018/7/15 23:41
 * @description:
 */
@Data
public class RpcRequest {

    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

}
