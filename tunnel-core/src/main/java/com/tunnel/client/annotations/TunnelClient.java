package com.tunnel.client.annotations;

import com.tunnel.support.coder.Decoder;
import com.tunnel.support.coder.DefaultDecoder;
import com.tunnel.support.coder.DefaultEncoder;
import com.tunnel.support.coder.Encoder;
import okhttp3.Interceptor;

import java.lang.annotation.*;

/**
 * HTTP请求客户端
 *
 * @author yq
 * @since 2024/4/16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TunnelClient {

    /**
     * 名称
     *
     * @return name
     */
    String name() default "";

    /**
     * 请求地址
     *
     * @return url
     */
    String url() default "";

    /**
     * 编码器
     *
     * @return Encoder
     */
    Class<? extends Encoder> encoder() default DefaultEncoder.class;

    /**
     * 解码器
     *
     * @return Decoder
     */
    Class<? extends Decoder> decoder() default DefaultDecoder.class;

    /**
     * 请求拦截器
     *
     * @return Interceptor[]
     */
    Class<? extends Interceptor>[] interceptors() default {};

}
