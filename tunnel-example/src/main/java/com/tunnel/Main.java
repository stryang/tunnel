package com.tunnel;

import com.tunnel.client.proxy.TunnelClientProxyInstanceHolder;


/**
 * @author yq
 * @since 2024/4/16
 */
public class Main {

    private static final String BASE_PACKAGE = "com.tunnel";

    public static void main(String[] args) {
        // 初始化代理客户端
        TunnelClientProxyInstanceHolder.initClientProxyInstance(BASE_PACKAGE);
        // 获取代理客户端
        TunnelClientExample client = TunnelClientProxyInstanceHolder.getClient(TunnelClientExample.class);
        client.get();
    }

}
