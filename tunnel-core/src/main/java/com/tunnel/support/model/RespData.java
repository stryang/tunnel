package com.tunnel.support.model;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author yq
 * @since 2024/4/16
 */
@Getter
public class RespData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String msg;
    private String traceId;
    private T data;

}
