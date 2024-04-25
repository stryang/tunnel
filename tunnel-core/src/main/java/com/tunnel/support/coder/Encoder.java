package com.tunnel.support.coder;

import okhttp3.RequestBody;

/**
 * 编码器
 *
 * @author yq
 * @since 2024/4/16
 */
public interface Encoder {

    /**
     * 编码
     *
     * @param value 请求参数
     * @return RequestBody
     */
    <T> RequestBody encode(T value);

}
