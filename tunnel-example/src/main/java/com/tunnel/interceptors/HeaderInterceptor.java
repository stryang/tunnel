package com.tunnel.interceptors;

import com.tunnel.model.constants.HeaderConstants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author yq
 * @since 2024/4/16
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder().addHeader(HeaderConstants.DEVICE_ID, "tunnel-client")
                .addHeader(HeaderConstants.LANG, "en-US");
        return chain.proceed(builder.build());
    }

}
