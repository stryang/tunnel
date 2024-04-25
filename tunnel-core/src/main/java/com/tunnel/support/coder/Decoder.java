package com.tunnel.support.coder;

import java.lang.reflect.Type;

/**
 * 解码器
 *
 * @author yq
 * @since 2024/4/16
 */
public interface Decoder {

    /**
     * 解码
     *
     * @param decodeStr 返回字符串
     * @param type      返回值类型
     * @return 返回值
     */
    <T> T decode(String decodeStr, Type type);

}
