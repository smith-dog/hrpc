package com.hunter.hrpc.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Auther: smith-dog
 * @Date: 2018/7/5 20:09
 * @Description:
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RpcService {
    Class<?> value();
}
