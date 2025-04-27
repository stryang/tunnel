package com.tunnel;

import com.tunnel.client.annotations.TunnelClient;
import com.tunnel.encoder.ExampleDecoder;
import com.tunnel.interceptors.HeaderInterceptor;
import com.tunnel.model.resp.CountryResp;
import com.tunnel.support.annotations.Get;

import java.util.List;

/**
 * @author yq
 * @since 2024/4/16
 */
@TunnelClient(
        name = "tunnelClientExample",
        url = "https://restcountries.com",
        decoder = ExampleDecoder.class,
        interceptors = HeaderInterceptor.class
)
public interface TunnelClientExample {

    /**
     * 查询接口
     */
    @Get("/v3.1/all?fields=capital")
    List<CountryResp> get();

}
