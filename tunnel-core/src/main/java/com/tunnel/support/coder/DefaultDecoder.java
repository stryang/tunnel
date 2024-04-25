package com.tunnel.support.coder;

import com.alibaba.fastjson.JSON;
import com.tunnel.support.exceptions.DecodeException;
import com.tunnel.support.model.RespData;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yq
 * @since 2024/4/16
 */
@Slf4j
public class DefaultDecoder implements Decoder {


    private final static Object LOCK = new Object();
    private final static Map<Type, ParameterizedTypeImpl> TYPE_PARAMETERIZED_CACHE = new HashMap<>();

    @Override
    public <T> T decode(String decodeStr, Type type) {
        try {
            ParameterizedTypeImpl parameterizedType = getParameterizedType(type);
            RespData<T> respData = JSON.parseObject(decodeStr, parameterizedType);
            return respData.getData();
        } catch (Exception e) {
            log.error("decode error", e);
            throw new DecodeException(e.getMessage());
        }
    }

    private ParameterizedTypeImpl getParameterizedType(Type type) {
        ParameterizedTypeImpl parameterizedType = TYPE_PARAMETERIZED_CACHE.get(type);
        if (Objects.nonNull(parameterizedType)) {
            return parameterizedType;
        }
        synchronized (LOCK) {
            parameterizedType = TYPE_PARAMETERIZED_CACHE.get(type);
            if (Objects.nonNull(parameterizedType)) {
                return parameterizedType;
            }
            parameterizedType = ParameterizedTypeImpl.make(RespData.class, new Type[]{type}, null);
            TYPE_PARAMETERIZED_CACHE.put(type, parameterizedType);
        }
        return parameterizedType;
    }

}
