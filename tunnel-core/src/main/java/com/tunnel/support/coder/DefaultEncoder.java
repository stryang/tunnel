package com.tunnel.support.coder;

import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.Objects;

/**
 * @author yq
 * @since 2024/4/16
 */
public class DefaultEncoder implements Encoder {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public <T> RequestBody encode(T value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONString(value));
    }

}
