package com.tunnel;

import com.tunnel.client.annotations.TunnelClient;
import com.tunnel.client.proxy.TunnelClientProxyInstanceHolder;
import com.tunnel.configuration.TunnelClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * @author yq
 * @since 2024/4/24
 */
@Slf4j
@RequiredArgsConstructor
public class TunnelClientRegistry implements SmartInitializingSingleton, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;
    private final TunnelClientProperties tunnelClientProperties;

    @Override
    public void afterSingletonsInstantiated() {
        // 初始化代理客户端
        TunnelClientProxyInstanceHolder.initClientProxyInstance(tunnelClientProperties.getScanPackage());
        // 获取所有客户端
        Map<Class<?>, Object> allTunnelClients = TunnelClientProxyInstanceHolder.getAll();
        allTunnelClients.forEach((k, v) -> {
            TunnelClient tunnelClient = k.getAnnotation(TunnelClient.class);
            Objects.requireNonNull(tunnelClient);
            String beanName = tunnelClient.name();
            Assert.hasText(beanName, "tunnel client name can not be empty!");
            if (!beanFactory.containsBeanDefinition(beanName)) {
                beanFactory.registerSingleton(beanName, v);
            } else {
                log.warn("bean {} already exists.", beanName);
            }
        });
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
