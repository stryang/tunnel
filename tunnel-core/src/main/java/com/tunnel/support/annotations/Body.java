package com.tunnel.support.annotations;

import java.lang.annotation.*;

/**
 * @author yq
 * @since 2024/4/16
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Body {
}
