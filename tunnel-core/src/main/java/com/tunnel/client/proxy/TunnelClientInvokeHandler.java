package com.tunnel.client.proxy;

import com.tunnel.client.annotations.TunnelClient;
import com.tunnel.support.annotations.Delete;
import com.tunnel.support.annotations.Get;
import com.tunnel.support.annotations.Post;
import com.tunnel.support.annotations.Put;
import com.tunnel.support.coder.Decoder;
import com.tunnel.support.coder.Encoder;
import com.tunnel.support.enums.HttpMethod;
import com.tunnel.support.exceptions.MethodNotSupportedException;
import com.tunnel.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * @author yq
 * @since 2024/4/16
 */
@Slf4j
public class TunnelClientInvokeHandler<T> implements InvocationHandler {

    private final TunnelClient tunnelClient;
    private OkHttpClient client;

    public TunnelClientInvokeHandler(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        this.tunnelClient = clazz.getAnnotation(TunnelClient.class);
        Objects.requireNonNull(this.tunnelClient);
        initHttpClient();
    }

    private void initHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        for (Class<? extends Interceptor> interceptor : tunnelClient.interceptors()) {
            builder.addInterceptor(ClassUtils.getInstance(interceptor));
        }
        this.client = builder.build();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 方法返回值类型
        Type type = method.getGenericReturnType();
        // 查找注解
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            HttpMethod httpMethod = HttpMethod.of(annotation.annotationType().getSimpleName());
            if (Objects.isNull(httpMethod)) {
                continue;
            }
            switch (httpMethod) {
                case POST:
                    Post post = (Post) annotation;
                    return execute(post.value(), args[0], type,
                            (url, body) -> new Request.Builder()
                                    .post(body)
                                    .url(url)
                                    .build());
                case GET:
                    Get get = (Get) annotation;
                    return execute(get.value(), null, type,
                            (url, body) -> new Request.Builder()
                                    .url(url)
                                    .build());
                case DELETE:
                    Delete delete = (Delete) annotation;
                    return execute(delete.name(), args[0], type,
                            (url, body) -> new Request.Builder()
                                    .delete(body)
                                    .url(url)
                                    .build());
                case PUT:
                    Put put = (Put) annotation;
                    return execute(put.name(), args[0], type,
                            (url, body) -> new Request.Builder()
                                    .put(body)
                                    .url(url)
                                    .build());
                default:
            }
        }
        throw new MethodNotSupportedException("The method " + method.getName() + " is not supported");
    }

    <T> Object execute(String path, T params, Type type, BiFunction<String, RequestBody, Request> builder) {
        try {
            // 请求参数编码处理
            Class<? extends Encoder> encoderClazz = tunnelClient.encoder();
            Encoder encoder = ClassUtils.getInstance(encoderClazz);
            RequestBody requestBody = encoder.encode(params);

            // 构建HTTP请求
            String url = tunnelClient.url() + path;
            Request request = builder.apply(url, requestBody);
            long startTime = System.currentTimeMillis();
            Response response = client.newCall(request).execute();
            long endTime = System.currentTimeMillis();
            byte[] bytes = Objects.nonNull(response.body()) ? response.body().bytes() : new byte[0];
            String decodeStr = new String(bytes, "UTF-8");
            log.info("http request url={} time={}ms response={}", url, endTime - startTime, decodeStr);

            // 解析响应
            if (!response.isSuccessful()) {
                throw new RuntimeException("url " + url + " is not successful code is " + response.code());
            }
            ResponseBody body = response.body();
            if (Objects.isNull(body)) {
                log.warn("TunnelClientInvokeHandler invoke body is null");
                return null;
            }
            body.close();

            // 解码处理
            Class<? extends Decoder> decoderClazz = tunnelClient.decoder();
            Decoder decoder = ClassUtils.getInstance(decoderClazz);
            return decoder.decode(decodeStr, type);
        } catch (IOException ioException) {
            log.error("execute ioException", ioException);
            throw new RuntimeException(ioException);
        } catch (Exception exception) {
            log.error("execute exception", exception);
            throw new RuntimeException(exception);
        }

    }

}
