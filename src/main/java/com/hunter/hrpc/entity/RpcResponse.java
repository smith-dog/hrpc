package com.hunter.hrpc.entity;

import lombok.Data;

/**
 * @version 1.0.0
 * @author: hunter
 * @date: 2018/7/15 23:41
 * @description:
 */
@Data
public class RpcResponse {

    private String requestId;
    private Throwable error;
    private Object result;


    public boolean isError() {
        return error == null ? false : true ;
    }

}
