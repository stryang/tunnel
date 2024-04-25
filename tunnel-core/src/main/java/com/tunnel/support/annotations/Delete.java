package com.tunnel.support.annotations;

import java.lang.annotation.*;

/**
 * @author yq
 * @since 2024/4/16
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Delete {

    /**
     * 接口访问路径
     *
     * @return name
     */
    String name() default "";

    /**
     * 请求头设置
     *
     * @return headers
     */
    String[] headers() default {};

}
