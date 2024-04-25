package com.tunnel;

import com.tunnel.client.annotations.TunnelClient;
import com.tunnel.interceptors.HeaderInterceptor;
import com.tunnel.support.annotations.Get;

/**
 * @author yq
 * @since 2024/4/16
 */
@TunnelClient(name = "tunnelClientExample", url = "https://www.baidu.com", interceptors = HeaderInterceptor.class)
public interface TunnelClientExample {

    /**
     * 业务检测接口
     */
    @Get
    void get();

}
