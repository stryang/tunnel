package com.tunnel.configuration;

import com.tunnel.TunnelClientRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yq
 * @since 2024/4/24
 */
@Slf4j
@Configuration
public class TunnelAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TunnelClientProperties tunnelClientProperties() {
        log.info("TunnelClientProperties initialized.");
        return new TunnelClientProperties();
    }

    @Bean
    public TunnelClientRegistry tunnelClientRegistry(TunnelClientProperties tunnelClientProperties) {
        log.info("TunnelClientRegistry initialized.");
        return new TunnelClientRegistry(tunnelClientProperties);
    }

}
