package com.tunnel.client.proxy;

import com.tunnel.client.annotations.TunnelClient;
import com.tunnel.utils.ClassUtils;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yq
 * @since 2024/4/16
 */
public class TunnelClientProxyInstanceHolder {

    private static final Map<Class<?>, Object> CLIENT_PROXY_INSTANCE_HOLDER = new ConcurrentHashMap<>();

    public static void initClientProxyInstance(String packageName) {
        List<Class<?>> clientClasses = ClassUtils.findAnnotated(packageName, TunnelClient.class);
        for (Class<?> clazz : clientClasses) {
            Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new TunnelClientInvokeHandler<>(clazz));
            CLIENT_PROXY_INSTANCE_HOLDER.put(clazz, instance);
        }
    }

    public static Map<Class<?>, Object> getAll() {
        return CLIENT_PROXY_INSTANCE_HOLDER;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getClient(Class<T> clazz) {
        return (T) CLIENT_PROXY_INSTANCE_HOLDER.get(clazz);
    }

}
