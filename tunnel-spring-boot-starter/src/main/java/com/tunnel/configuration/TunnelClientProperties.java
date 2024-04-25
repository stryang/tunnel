package com.tunnel.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yq
 * @since 2024/4/24
 */
@Data
@ConfigurationProperties(prefix = "tunnel.config")
public class TunnelClientProperties {

    /**
     * 扫描包路径
     */
    private String scanPackage = "com.tunnel";

}
