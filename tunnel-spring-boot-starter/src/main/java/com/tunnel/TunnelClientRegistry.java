package com.tunnel;

import com.tunnel.client.annotations.TunnelClient;
import com.tunnel.client.proxy.TunnelClientInvokeHandler;
import com.tunnel.configuration.TunnelClientProperties;
import com.tunnel.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;

/**
 * @author yq
 * @since 2024/4/24
 */
@Slf4j
@RequiredArgsConstructor
public class TunnelClientRegistry implements BeanFactoryPostProcessor {

    private final TunnelClientProperties tunnelClientProperties;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        List<Class<?>> clientClasses = ClassUtils.findAnnotated(tunnelClientProperties.getScanPackage(), TunnelClient.class);

        for (Class<?> clazz : clientClasses) {
            TunnelClient tunnelClient = clazz.getAnnotation(TunnelClient.class);
            Objects.requireNonNull(tunnelClient);

            String beanName = tunnelClient.name();
            Assert.hasText(beanName, "tunnel client name can not be empty!");

            if (!beanFactory.containsBeanDefinition(beanName)) {
                Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new TunnelClientInvokeHandler<>(clazz));
                beanFactory.registerSingleton(beanName, instance);
                log.info("register bean {}.", beanName);
            } else {
                log.warn("bean {} already exists.", beanName);
            }
        }
    }

}
