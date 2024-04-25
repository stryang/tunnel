package com.tunnel.support.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yq
 * @since 2024/4/16
 */
@Getter
@RequiredArgsConstructor
public class DecodeException extends RuntimeException {

    private final String msg;

    public DecodeException() {
        this.msg = "";
    }

}
